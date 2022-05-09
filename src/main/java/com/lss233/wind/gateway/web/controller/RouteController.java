package com.lss233.wind.gateway.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.service.impl.RouteServiceImpl;
import io.javalin.http.Context;

import java.util.Objects;


/**
 * @author zzl
 * @date 2022/4/29 22:41
 */
public class RouteController {

    public static RouteService routeService = new RouteServiceImpl();

    /**
     * 配置路由，如果存在则修改，不存在创建
     * @param context
     * @throws JsonProcessingException
     */
    public static void setRoute(Context context) throws JsonProcessingException {
        context.json(routeService.setRoute(context));
    }

    /**
     * 查询路由信息。
     * @param context
     */
    public static void getRoute(Context context) throws JsonProcessingException {
        context.json(routeService.getRoute(context));
    }

    /**
     * 展示所有路由信息
     */
    public static void getAllRoute(Context context) throws JsonProcessingException {
        context.json(routeService.getAllRoutes(context));
    }

    /**
     * 删除指定路由名的路由
     * @param context
     */
    public static void deleteRoute(Context context) throws JsonProcessingException {
        String routeName = context.formParam("routeName");
        context.json(routeService.deleteRoute(routeName));
    }

    /**
     * 路由上下线
     * @param context
     * @throws JsonProcessingException
     */
    public static void onOrOffline(Context context) throws JsonProcessingException {
        String routeName = context.formParam("routeName");
        Integer isPublish = Integer.valueOf(Objects.requireNonNull(context.formParam("isPublish")));

        context.json(routeService.onOrOffline(routeName, isPublish));
    }
}
