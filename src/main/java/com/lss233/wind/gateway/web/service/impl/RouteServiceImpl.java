package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.web.dao.RouteConsulDao;
import com.lss233.wind.gateway.web.entity.RouteView;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;
import io.javalin.http.Context;
import io.netty.util.internal.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzl
 * @date 2022/5/5 20:37
 */
public class RouteServiceImpl implements RouteService {

    @Override
    public MyResult<RouteView> setRoute(Context context) throws JsonProcessingException {
        RouteView routeView = context.bodyAsClass(RouteView.class);
        if (routeView == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(),"routeView不可为null", routeView);
        }
        Route route = routeView.getRoute();
        HttpRoute httpRoute = routeView.getHttpRoute();
        if (!RouteInfo.setRoute(route)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由设置失败", routeView);
        }
        if (!RouteInfo.setHttpRoute(route.getName(), httpRoute)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由主机和路径设置失败", routeView);
        }
        return MyResult.success(routeView);
    }

    @Override
    public MyResult<RouteView> getRoute(Context context) throws JsonProcessingException {
        RouteView routeView = new RouteView();
        String routeName = context.pathParam("routeName");
        System.out.println("12345679:"+routeName);
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
        routeView.setRoute(target);
        HttpRoute httpRoute = RouteInfo.getHttpRoute(target.getName());
        routeView.setHttpRoute(httpRoute);
        return MyResult.success(routeView);
    }

    @Override
    public MyResult<List<RouteView>> getAllRoutes(Context context) throws JsonProcessingException {
        List<Route> routes = RouteInfo.getRoute();
        List<RouteView> routeViews = new ArrayList<>();
        if (routes.isEmpty()) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        RouteView routeView = new RouteView();
        for (Route route : routes) {
            routeView.setRoute(route);
            HttpRoute httpRoute = RouteInfo.getHttpRoute(route.getName());
            routeView.setHttpRoute(httpRoute);
            routeViews.add(routeView);
        }
        return MyResult.success(routeViews);
    }

    @Override
    public MyResult deleteRoute(String routeName) throws JsonProcessingException {
        if (StringUtil.isNullOrEmpty(routeName)) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(),"routeName不可为空",null);
        }
        boolean isDel = RouteInfo.delRoute(routeName);
        if (!isDel){
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由删除失败",null);
        }
        if (RouteInfo.delHttpRoute(routeName)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(),"路由主机和路径删除失败", null);
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
