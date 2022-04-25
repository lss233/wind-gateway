package com.lss233.wind.gateway.service.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class HttpForwardBackendHandler extends SimpleChannelInboundHandler<HttpObject> {
    private ChannelHandlerContext ctxClientSide;
    private HttpRequest request;
    private HttpRoute route;
    private HttpClient client;
    public HttpForwardBackendHandler(ChannelHandlerContext ctx, HttpRequest req, HttpRoute route, HttpClient client) {
        this.ctxClientSide = ctx;
        this.request = req;
        this.route = route;
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctxServerSide, HttpObject msg) throws Exception {
        if(msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            ctxClientSide.channel().writeAndFlush(response);
        }
        if(msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            ctxClientSide.channel().writeAndFlush(content.copy());

            if(msg instanceof LastHttpContent) {
                ctxServerSide.close();
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        ctxClientSide.flush();
        super.channelReadComplete(ctx);
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
