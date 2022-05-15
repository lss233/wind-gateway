package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.http.HttpClient;
import com.lss233.wind.gateway.service.http.HttpForwardBackendHandler;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.service.http.filter.PostHttpFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author : yjp
 * @Date : 2022/5/5 1:08
 */
public class gRPCForwardBackendHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger LOG = LoggerFactory.getLogger(gRPCForwardBackendHandler.class);
    private ChannelHandlerContext ctxClientSide;
    private HttpRequest request;
    private HttpRoute route;
    private gRPCClient client;
    private boolean isClosed = false;
    public gRPCForwardBackendHandler(ChannelHandlerContext ctx, HttpRequest req, HttpRoute route, gRPCClient client) {
        this.ctxClientSide = ctx;
        this.request = req;
        this.route = route;
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctxServerSide, HttpObject msg) throws Exception {
        LOG.debug("Receive upstream response {}", msg);

        // 执行路由过滤器
        if(route != null) {
            for (Filter filter : route.getFilters()) {
                if(filter instanceof PostHttpFilter) {
                    ((PostHttpFilter) filter).onServerMessage(ctxServerSide, msg);
                    if(isClosed) {
                        break;
                    }
                }
            }
        }

        if(msg instanceof HttpResponse) {
            LOG.debug("Receive upstream response from ctx {} to client {} with response {}", ctxServerSide, ctxClientSide, msg);
            HttpResponse response = (HttpResponse) msg;
            ctxClientSide.channel().write(response);
        }
        if(msg instanceof HttpContent) {
            LOG.debug("Receive upstream content from ctx {} to client {} with content {}", ctxServerSide, ctxClientSide, msg);
            HttpContent content = (HttpContent) msg;
            ctxClientSide.channel().write(content.copy());
            if(msg instanceof LastHttpContent) {
                ctxServerSide.flush();
                ctxServerSide.close();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctxServerSide) throws Exception {
        LOG.debug("channelReadComplete from ctx {} to client {}", ctxServerSide, ctxClientSide);
        ctxClientSide.flush();
        super.channelReadComplete(ctxServerSide);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }
}