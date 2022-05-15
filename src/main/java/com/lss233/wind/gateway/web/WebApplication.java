package com.lss233.wind.gateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.UpstreamInfo;
import com.lss233.wind.gateway.web.controller.RouteController;
import com.lss233.wind.gateway.web.controller.ServiceController;
import com.lss233.wind.gateway.web.controller.UpstreamController;
import com.lss233.wind.gateway.web.controller.UserController;
import com.lss233.wind.gateway.web.entity.AccessURL;
import com.lss233.wind.gateway.web.entity.User;
import com.lss233.wind.gateway.web.interceptor.ApiAccessManager;
import io.javalin.Javalin;
import io.javalin.core.util.Headers;
import io.javalin.http.Context;


import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * @author zzl
 * @date 2022/4/29 22:24
 */
public class WebApplication {

    public static void webService() throws JsonProcessingException {

        System.out.println("web");
        //存入管理员到consul
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("123456");
        admin.getMyUrls().add(AccessURL.ADMIN);

        ConsulApi api = new ConsulApi();

        //序列化
        ObjectMapper objectMapper = new ObjectMapper();
        String routeJson = objectMapper.writeValueAsString(admin);

        //将keyValue存入consul中
        api.setKVValue(admin.getUsername(),routeJson);


        //服务api
        Javalin app = Javalin.create(config -> config.accessManager(new ApiAccessManager())).start(7000);
        app.routes(() -> {
            path("user", () -> {
//                post("register", UserController::register, AccessURL.ANYONE);
                post("login", UserController::login, AccessURL.ANYONE);
            });
            path("route", () -> {
                post("/setRoute", RouteController::setRoute, AccessURL.ADMIN);
                put("/update", RouteController::setRoute, AccessURL.ADMIN);
                get("/getRoute/{routeName}", RouteController::getRoute, AccessURL.ADMIN);
                get("/getRoute", RouteController::getAllRoute, AccessURL.ADMIN);
                put("/deleteRoute", RouteController::deleteRoute, AccessURL.ADMIN);
                put("/online", RouteController::online, AccessURL.ADMIN);
                post("/search", RouteController::search, AccessURL.ADMIN);
            });
            path("upstream", () -> {
                post("/setUpstream", UpstreamController::setUpstream, AccessURL.ADMIN);
                put("/update", UpstreamController::setUpstream, AccessURL.ADMIN);
                get("/getUpstream/{upstreamName}", UpstreamController::getUpstream, AccessURL.ADMIN);
                get("/getUpstream", UpstreamController::getUpstreams, AccessURL.ADMIN);
                put("/deleteUpstream", UpstreamController::deleteUpstream, AccessURL.ADMIN);
                post("/search", UpstreamController::search, AccessURL.ADMIN);
            });
            path("service", () -> {
                post("/register", ServiceController::registerService, AccessURL.ADMIN);
                put("/updateService", ServiceController::updateService, AccessURL.ADMIN);
                get("/getService/{serviceName}", ServiceController::getService, AccessURL.ADMIN);
                get("/getServices", ServiceController::getServices, AccessURL.ADMIN);
                put("/deleteService", ServiceController::deregister, AccessURL.ADMIN);
                post("/search", ServiceController::search, AccessURL.ADMIN);
            });
        });
    }
}
