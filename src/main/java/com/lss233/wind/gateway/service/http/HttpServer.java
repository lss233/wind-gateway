package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.common.config.ReadConfiguration;
import com.lss233.wind.gateway.common.type.Service;
import com.lss233.wind.gateway.service.consul.Cache.ScheduledTasks;
import com.lss233.wind.gateway.service.http.filter.FilterRegistry;
import com.lss233.wind.gateway.web.WebApplication;
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

@Service
public class HttpServer {
    private final static Logger LOG = LoggerFactory.getLogger(HttpServer.class);
    private static FilterRegistry filterRegistry;

    public void start() throws Exception {
        filterRegistry = new FilterRegistry();
        filterRegistry.init();

        LOG.info("Loading settings...");
        // 读取配置信息
        ReadConfiguration readConfiguration = new ReadConfiguration();
        readConfiguration.readYamlConfiguration();

        // 读取数据
        Runnable runnable = new ScheduledTasks();
        Thread thread = new Thread(runnable, "Loading to Cache...");
        // 启动线程定时任务刷新到缓存
        thread.start();

        WebApplication.webService();
        LOG.info("Starting...");
        // 启动服务器
        ServerBootstrap bootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        bootstrap.group(boss,work)
                .handler(new LoggingHandler(LogLevel.DEBUG))
                .channel(NioServerSocketChannel.class)
                .childHandler(new HttpServerInitializer(null));

        ChannelFuture f = bootstrap.bind(new InetSocketAddress(ReadConfiguration.Config.getServicePort())).sync();
        LOG.info("HTTP server start up at port " + ReadConfiguration.Config.getServicePort());
        f.channel().closeFuture().sync();


    }
}
