package com.lss233.wind.gateway.service.http;

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
import java.net.URI;
import java.net.URISyntaxException;

public class HttpClient {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClient.class);
    private SslContext sslCtx = null;
    private final EventLoopGroup group;
    private final Bootstrap bootstrap;
    private final Upstream upstream;
    private final Upstream.Destination destination;
    private final Scheme scheme;
    private Channel channel = null;

    private HttpClient(Builder builder) throws URISyntaxException, SSLException, InterruptedException {
        this.upstream = builder.upstream;
        scheme = upstream.getScheme();
        destination = upstream.chooseDestination();
        if(upstream.getScheme() instanceof HttpsScheme) {
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
                .handler(new HttpClientInitializer(sslCtx));
        LOG.debug("Connecting to upstream {}:{}", destination.getHost(), destination.getPort());
        channel = bootstrap.connect(destination.getHost(), destination.getPort()).sync().channel();
        LOG.debug("Connection started");
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
        private Upstream upstream;
        private Builder() {
            // Hide constructor
        }
        public HttpClient build() throws SSLException, URISyntaxException, InterruptedException {
            return new HttpClient(this);
        }

        public Builder upstream(Upstream upstream) {
            this.upstream = upstream;
            return this;
        }
    }
}
