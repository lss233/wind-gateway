package com.lss233.wind.gateway.service.http.grpc;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;
import io.netty.handler.ssl.SslContext;

/**
 * @Author : yjp
 * @Date : 2022/5/4 21:06
 */
public class gRPCClientInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;

    public gRPCClientInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpResponseDecoder());
        p.addLast(new HttpResponseDecoder());
    }
}
