package com.lss233.wind.gateway.web.service.impl;

import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.entity.KeyValue;
import com.lss233.wind.gateway.web.dao.RouteConsulDao;
import com.lss233.wind.gateway.web.service.RouteService;
import io.javalin.http.Context;

/**
 * @author zzl
 * @date 2022/5/5 20:37
 */
public class RouteServiceImpl implements RouteService {

    public RouteConsulDao routeConsulDao = new RouteConsulDao();

    @Override
    public void createRoute(Context context) throws JsonProcessingException {
        Route route = context.bodyAsClass(Route.class);
        String routeJson = routeConsulDao.storeRouteToConsul(route);
        context.result(routeJson);
        // TODO: 2022/5/5 查看测试数据，之后去掉
        System.out.println(routeJson);
    }

    @Override
    public void getRoute(Context context) {
        String key = context.pathParam("key");
        ConsulApi api = new ConsulApi();
        Response<GetValue> route = api.getSingleKVForKey(key);
        context.json(route);
    }
}
