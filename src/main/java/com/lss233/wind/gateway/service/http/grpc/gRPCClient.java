package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.common.Scheme;
import com.lss233.wind.gateway.common.Upstream;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.net.URISyntaxException;

/**
 * @Author : yjp
 * @Date : 2022/5/4 21:02
 */
public class gRPCClient {
    private static final Logger LOG = LoggerFactory.getLogger(gRPCClient.class);
    private SslContext sslCtx = null;
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final Upstream upstream;
    private final Upstream.Destination destination;
    private final Scheme scheme;
    private Channel channel = null;

    private gRPCClient(gRPCClient.Builder builder) throws URISyntaxException, SSLException, InterruptedException {
        this.upstream = builder.upstream;
        scheme = upstream.getScheme();
        destination = upstream.chooseDestination();
        if(upstream.getScheme() instanceof gRPCScheme) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();

        // Configure the client.
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new gRPCClientInitializer(sslCtx));
        LOG.debug("Connecting to upstream {}:{}", destination.getHost(), destination.getPort());
        channel = bootstrap.connect(destination.getHost(), destination.getPort()).sync().channel();
        LOG.debug("Connection started");
    }

    public static gRPCClient.Builder builder() {
        return new gRPCClient.Builder();
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
        private Upstream upstream;
        private Builder() {
            // Hide constructor
        }
        public gRPCClient build() throws SSLException, URISyntaxException, InterruptedException {
            return new gRPCClient(this);
        }

        public gRPCClient.Builder upstream(Upstream upstream) {
            this.upstream = upstream;
            return this;
        }
    }
}
