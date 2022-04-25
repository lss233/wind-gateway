package com.lss233.wind.gateway.service.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClient {
    private SslContext sslCtx = null;
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final URI uri;
    private String host;
    private int port;
    private Channel channel = null;

    private HttpClient(Builder builder) throws URISyntaxException, SSLException, InterruptedException {
        this.uri = builder.uri;
        String scheme = uri.getScheme() == null? "http" : uri.getScheme();
        String host = uri.getHost() == null ? "127.0.0.1" : uri.getHost();
        int port = uri.getPort();
        if (port == -1) {
            if ("http".equalsIgnoreCase(scheme)) {
                port = 80;
            } else if ("https".equalsIgnoreCase(scheme)) {
                port = 443;
            }
        }
        if ("http".equalsIgnoreCase(scheme)) {
            sslCtx = null;
        } else if ("https".equalsIgnoreCase(scheme)) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        }

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        // Configure the client.
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new HttpClientInitializer(sslCtx));
        channel = bootstrap.connect(host, port).sync().channel();
    }

    public static Builder builder() {
        return new Builder();
    }

    public void close() throws InterruptedException {
        if(channel == null) {
            throw new IllegalStateException("Client not started yet.");
        }
        channel.closeFuture().sync();
        group.shutdownGracefully();
    }
    public Channel getChannel() {
        return channel;
    }
    public static class Builder {
        private URI uri;
        private Builder() {
            // Hide constructor
        }
        public Builder url(URI uri) {
            this.uri = uri;
            return this;
        }
        public HttpClient build() throws SSLException, URISyntaxException, InterruptedException {
            return new HttpClient(this);
        }
    }
}
