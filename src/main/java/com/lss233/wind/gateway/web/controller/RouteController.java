package com.lss233.wind.gateway.web.controller;

import com.lss233.wind.gateway.common.Scheme;
import com.lss233.wind.gateway.common.SchemeRegistry;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.service.impl.RouteServiceImpl;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;


/**
 * @author zzl
 * @date 2022/4/29 22:41
 */
@Slf4j
public class RouteController {

    public static RouteService routeService = new RouteServiceImpl();

    /**
     * 配置路由，如果存在则修改，不存在创建
     * @param context
     */
    public static void setRoute(Context context) throws InstantiationException, IllegalAccessException {
        HttpRoute httpRoute = context.bodyAsClass(HttpRoute.class);
        Class<? extends Scheme> schemeClass = SchemeRegistry.getInstance().getRegistry(context.formParam("scheme"));
        Scheme scheme = schemeClass.newInstance();
        System.out.println(scheme.getName());
        context.json(routeService.setRoute(httpRoute));
    }

    /**
     * 查询路由信息。
     * @param context
     */
    public static void getRoute(Context context) {
        String routeName = context.pathParam("routeName");
        context.json(routeService.getRoute(routeName));
    }

    /**
     * 展示所有路由信息
     */
    public static void getAllRoute(Context context) {
        context.json(routeService.getAllRoutes());
    }

    /**
     * 删除指定路由名的路由
     * @param context
     */
    public static void deleteRoute(Context context) {
        String routeName = context.formParam("routeName");
        context.json(routeService.deleteRoute(routeName));
    }

    /**
     * 路由上下线
     * @param context
     */
    public static void online(Context context) {
        String routeName = context.formParam("routeName");

        context.json(routeService.onOrOffline(routeName));
    }

    public static void search(Context context) {
        String routeName = context.formParam("routeName");
        String path = context.formParam("path");

        context.json(routeService.search(routeName, path));
    }
}
