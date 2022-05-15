package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.common.lb.RandomLoadBalancer;
import com.lss233.wind.gateway.service.consul.Cache.HttpRouteCache;
import com.lss233.wind.gateway.service.consul.Cache.UpstreamCache;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.service.http.filter.*;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HttpForwardFrontendHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpForwardFrontendHandler.class);
    private static final String CRLF = "\r\n";
    private HttpRequest request;
    private HttpRoute route;
    private HttpClient client;
    private boolean isClosed = false;
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
            LOG.debug("Accept client request from context {} with {}", ctx, request);
            route = parseRoute(request);
        }
        // 执行路由过滤器
        if(route != null) {
            for (Filter filter : route.getFilters()) {
                filter.setRoute(route);
                if(filter instanceof PreHttpFilter) {
                    ((PreHttpFilter) filter).onClientMessage(ctx, msg);
                    if(isClosed) {
                        break;
                    }
                }
            }
        }
        if(msg instanceof HttpRequest) {
            if (client == null) {
                client = HttpClient.builder()
                        .upstream(route.getUpstream())
                        .build();
                client.getChannel().pipeline().addLast(new HttpForwardBackendHandler(ctx, request, route, client));
                client.getChannel().writeAndFlush(request);
                client.getChannel().writeAndFlush(CRLF);
                client.getChannel().writeAndFlush(CRLF);
            }
        }

        if(msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            client.getChannel().writeAndFlush(content);
            LOG.debug("Receive client content from context {} with {}", ctx, content);
            if (msg instanceof LastHttpContent) {
                // TODO(lss233): 客户端发送了最后一条消息
            }
        }
    }

    // TODO 以下只是用于测试的数据
    private HttpRoute parseRoute(HttpRequest req) throws Exception {
        for (HttpRoute route : HttpRouteCache.getHttpRoutes()) {
            for (PathMatch pathMatch : route.getPathMatch()) {
                if(pathMatch.isMatch(req)) {
                    return route;
                }
            }
        }

        List<Upstream.Destination> endpoints = new ArrayList<>();
        endpoints.add(new Upstream.Destination("192.168.1.5", 8080, 1, true));

        Upstream upstream = new Upstream();
        upstream.setName("test upstream");
        upstream.setConnectTimeout(5000);
        upstream.setEndpoints(endpoints);
//        upstream.setScheme(new HttpsScheme());
        upstream.setScheme(new HttpScheme());
        upstream.SetLoadBalancerClass(RandomLoadBalancer.class);

        HttpRoute route = new HttpRoute();
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter()));
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new FlowLimitFilter())); //限流
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new IpRestriction())); //IP拦截
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new RefererRestriction())); //Referer拦截
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new UaRestriction())); // User-Agent拦截
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new UriBlocker()));  //Uri拦截
//        route.setFilters(Arrays.asList(new RewriteHeadersFilter(), new IpAccept()));  //IP允许
        route.setName("test");
        route.setPublish(true);
        route.setUpstream(upstream);
        return route;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_GATEWAY, Unpooled.wrappedBuffer("Bad Gateway".getBytes(StandardCharsets.UTF_8)))).addListener(ChannelFutureListener.CLOSE);
        cause.printStackTrace();
        ctx.writeAndFlush(cause).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        isClosed = true;
        super.channelUnregistered(ctx);
    }
}
