package com.lss233.wind.gateway.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.web.controller.RouteController;
import com.lss233.wind.gateway.web.controller.UserController;
import com.lss233.wind.gateway.web.entity.AccessURL;
import com.lss233.wind.gateway.web.entity.User;
import com.lss233.wind.gateway.web.interceptor.ApiAccessManager;
import io.javalin.Javalin;

import java.util.LinkedHashSet;
import java.util.Set;

import static io.javalin.apibuilder.ApiBuilder.*;

/**
 * @author zzl
 * @date 2022/4/29 22:24
 */
public class WebApplication {

    public static void main(String[] args) throws JsonProcessingException {

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
            path("route", () -> {
                post("createRoute", RouteController::createRoute, AccessURL.ADMIN);
                get("getRoute/{routeKey}", RouteController::getRoute, AccessURL.ADMIN);
            });
            path("user", () -> {
                post("register", UserController::register, AccessURL.ANYONE);
                get("login", UserController::login, AccessURL.ANYONE);
            });
        });
    }
}
