package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.service.http.HttpServer;
import com.lss233.wind.gateway.service.http.HttpServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @Author : yjp
 * @Date : 2022/5/5 1:20
 */
public class gRPCServer {
    private final static Logger LOG = LoggerFactory.getLogger(gRPCServer.class);

    public void start() throws Exception {
        LOG.info("Loading settings...");
        // 读取数据
        LOG.info("Starting...");
        // 启动服务器
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        bootstrap.group(boss,work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new gRPCServerInitializer(null));

        ChannelFuture f = bootstrap.bind(new InetSocketAddress(80)).sync();
        LOG.info("gRPC server start up at port " + 80);
        f.channel().closeFuture().sync();

    }
}
