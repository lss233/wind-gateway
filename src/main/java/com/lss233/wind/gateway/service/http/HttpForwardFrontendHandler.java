package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.service.http.filter.PreFilter;
import com.lss233.wind.gateway.service.http.filter.RewriteHeadersFilter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

import java.net.URI;
import java.net.URISyntaxException;

public class HttpForwardFrontendHandler extends SimpleChannelInboundHandler<HttpObject> {
    private HttpRequest request;
    private HttpRoute route;
    private HttpClient client;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest) {
            HttpRequest request = this.request = (HttpRequest) msg;
            if (HttpUtil.is100ContinueExpected(request)) {
                // 来，接着发
                ctx.write(new DefaultFullHttpResponse(
                        HttpVersion.HTTP_1_1,
                        HttpResponseStatus.CONTINUE));
            }
            route = parseRoute(request);
        }
        if(msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            //TODO(lss233): 转发前处理器
            PreFilter filter = new RewriteHeadersFilter();
            if(client == null) {
                client = HttpClient.builder()
                        .url(route.getUri())
                        .build();
                client.getChannel().pipeline().addLast(new HttpForwardBackendHandler(ctx, request, route, client));
                request.headers().set("Host", "www.lss233.com");
                request.headers().add("X-Request-Provided-by", "Wind-Gateway 1.0");
                request = (HttpRequest) filter.onClientMessage(request);
                client.getChannel().writeAndFlush(request);
            }
            content = (HttpContent) filter.onClientMessage(content);
            client.getChannel().writeAndFlush(content);
            if (msg instanceof LastHttpContent) {
                // TODO(lss233): 客户端发送了最后一条消息
            }
        }
    }

    // TODO
    private HttpRoute parseRoute(HttpRequest req) throws URISyntaxException {
        HttpRoute route = new HttpRoute();
        route.setName("test");
        route.setPublish(true);
        route.setUri(new URI("https://lss233.com/showheader.php"));
//        route.setUri(new URI("https://www.qq.com"));
//        route.setUri(new URI("http://localhost:8081/showheader.php"));
        return route;
    }
}
