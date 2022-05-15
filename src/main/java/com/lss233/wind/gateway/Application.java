package com.lss233.wind.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.http.HttpServer;
import com.lss233.wind.gateway.web.WebApplication;
import com.lss233.wind.gateway.web.controller.RouteController;
import com.lss233.wind.gateway.web.controller.ServiceController;
import com.lss233.wind.gateway.web.controller.UpstreamController;
import com.lss233.wind.gateway.web.controller.UserController;
import com.lss233.wind.gateway.web.entity.AccessURL;
import com.lss233.wind.gateway.web.entity.User;
import com.lss233.wind.gateway.web.interceptor.ApiAccessManager;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;
import static io.javalin.apibuilder.ApiBuilder.delete;

public class Application {
    private final static Logger LOG = LoggerFactory.getLogger(Application.class);
    public static void main(String[] args) throws Exception {
        LoggerFactory.getLogger("GWBanner").info("Start blowing...\n" +
                "\n" +
                "m     m   \"               #           mmm m     m\n" +
                "#  #  # mmm    m mm    mmm#         m\"   \"#  #  #\n" +
                "\" #\"# #   #    #\"  #  #\" \"#         #   mm\" #\"# #\n" +
                " ## ##\"   #    #   #  #   #         #    # ## ##\"\n" +
                " #   #  mm#mm  #   #  \"#m##          \"mmm\" #   #\n");
        LOG.info("Loading services...");
        HttpServer bootstrap  = new HttpServer();
        bootstrap.start();
    }
}
