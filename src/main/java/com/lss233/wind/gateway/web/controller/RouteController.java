package com.lss233.wind.gateway.web.controller;

import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.entity.KeyValue;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.service.impl.RouteServiceImpl;
import io.javalin.http.Context;


/**
 * @author zzl
 * @date 2022/4/29 22:41
 */
public class RouteController {

    public static RouteService routeService = new RouteServiceImpl();

    /**
     * 创建路由信息
     * @param context
     * @throws JsonProcessingException
     */
    public static void createRoute(Context context) throws JsonProcessingException {
        routeService.createRoute(context);
    }

    /**
     * 查询路由信息。
     * @param context
     */
    public static void getRoute(Context context) {
        routeService.getRoute(context);
    }
}
