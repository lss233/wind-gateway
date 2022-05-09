package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.web.dao.RouteConsulDao;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;
import io.javalin.http.Context;
import io.netty.util.internal.StringUtil;

import java.util.List;

/**
 * @author zzl
 * @date 2022/5/5 20:37
 */
public class RouteServiceImpl implements RouteService {

    public RouteConsulDao routeConsulDao = new RouteConsulDao();

    @Override
    public MyResult<Route> setRoute(Context context) throws JsonProcessingException {
        Route route = context.bodyAsClass(Route.class);
        System.out.println(route.getDescription());
        if (route == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(),"route不可为null", null);
        }
        if (!RouteInfo.setRoute(route)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由设置失败", route);
        }else {
            return MyResult.success(route);
        }
    }

    @Override
    public MyResult<Route> getRoute(Context context) throws JsonProcessingException {
        String routeName = context.pathParam("routeName");
        System.out.println("12345679:"+routeName);
        ConsulApi api = new ConsulApi();
        List<Route> routes = RouteInfo.getRoute();
        Route target = null;
        if (routes == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(), routeName+"未找到",null);
        }
        for (Route route : routes) {
            if (route.getName().equals(routeName)) {
                target = route;
                break;
            }
        }
        if (target == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(target);
    }

    @Override
    public MyResult<List<Route>> getAllRoutes(Context context) throws JsonProcessingException {
        List<Route> routes = RouteInfo.getRoute();
        if (routes.isEmpty()) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(routes);
    }

    @Override
    public MyResult deleteRoute(String routeName) throws JsonProcessingException {
        if (StringUtil.isNullOrEmpty(routeName)) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(),"routeName不可为空",null);
        }
        boolean isDel = RouteInfo.delRoute(routeName);
        if (!isDel){
            return MyResult.fail(ResultEnum.ERROR.getCode(), "删除失败",null);
        }
        return MyResult.success();
    }

    @Override
    public MyResult onOrOffline(String routeName ,Integer isPublish) throws JsonProcessingException {
        if (StringUtil.isNullOrEmpty(routeName) || isPublish == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由名和路由上下线设置均不可为空", null);
        }
        Route route = RouteInfo.getRoute(routeName);
        if (route == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        boolean flag = false;
        if (isPublish == 1) {
            flag = true;
        }
        route.setPublish(flag);
        return MyResult.success();
    }

}
