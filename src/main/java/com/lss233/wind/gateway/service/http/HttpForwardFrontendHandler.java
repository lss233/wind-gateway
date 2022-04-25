package com.lss233.wind.gateway.service.http;

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
            HttpContent httpContent = (HttpContent) msg;
            ByteBuf content = httpContent.content();
            //TODO(lss233): 转发前处理器

            if(client == null) {
                client = HttpClient.builder()
                        .url(route.getUri())
                        .build();
                client.getChannel().pipeline().addLast(new HttpForwardBackendHandler(ctx, request, route, client));
                request.headers().set("Host", "www.lss233.com");
                request.headers().add("X-Request-Provided-by", "Wind-Gateway 1.0");
                client.getChannel().writeAndFlush(request);
            }
            client.getChannel().writeAndFlush(content);
            if (msg instanceof LastHttpContent) {

            }
        }
//        // 获取请求的uri
//        String uri = req.uri();
//        String msg = "<html><head><title>test</title></head><body>你请求uri为：" + uri + "</body></html>";
//        // 找到对应的 Route
//        HttpRoute route = parseRoute(req);
//
////        client.getChannel().pipeline().addLast(new HttpTransferHandler(ctx, req, route, client));
//        HttpRequest request = req.copy();
//        ctx.writeAndFlush(request);
//        client.getChannel().writeAndFlush(request);
//        // 将html write到客户端
////        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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
