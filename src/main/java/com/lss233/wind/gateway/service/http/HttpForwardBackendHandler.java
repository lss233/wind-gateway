package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.http.filter.PostHttpFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpForwardBackendHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger LOG = LoggerFactory.getLogger(HttpForwardBackendHandler.class);
    private static final Logger REQUEST_LOGGER = LoggerFactory.getLogger("HttpForwarderBackend");

    private ChannelHandlerContext ctxClientSide;
    private HttpRequest request;
    private HttpRoute route;
    private HttpClient client;
    private boolean isClosed = false;
    public HttpForwardBackendHandler(ChannelHandlerContext ctx, HttpRequest req, HttpRoute route, HttpClient client) {
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
            REQUEST_LOGGER.info("{} - \"{}\" {} {} {} {} \"{}\"",
                    ctxClientSide.channel().remoteAddress(),
                    route.getName(),
                    response.protocolVersion(),
                    response.status().code(),
                    ctxServerSide.channel().remoteAddress(),
                    request.method(),
                    request.uri()
            );
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
