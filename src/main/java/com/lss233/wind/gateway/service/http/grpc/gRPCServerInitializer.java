package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.service.http.HttpForwardFrontendHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;

/**
 * @Author : yjp
 * @Date : 2022/5/5 1:22
 */
public class gRPCServerInitializer extends ChannelInitializer<SocketChannel> {
    /**
     * 最大 Content-Length 长度
     */
    private final static int MAX_CONTENT_LENGTH = 512 * 1024;

    private final SslContext sslCtx;

    public gRPCServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpRequestDecoder());
        p.addLast(new HttpResponseEncoder());
        p.addLast(new HttpForwardFrontendHandler());
    }
}
