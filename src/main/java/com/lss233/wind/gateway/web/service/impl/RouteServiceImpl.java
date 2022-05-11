package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.web.dao.RouteConsulDao;
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
    public MyResult<HttpRoute> setRoute(Context context) throws JsonProcessingException {
        HttpRoute httpRoute = context.bodyAsClass(HttpRoute.class);
        if (httpRoute == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(),"路由配置失败，路由信息不可为null", null);
        }
        if (!RouteInfo.setRoute(httpRoute)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由存入consul失败", httpRoute);
        }
        return MyResult.success(httpRoute);
    }

    @Override
    public MyResult<HttpRoute> getRoute(Context context) throws JsonProcessingException {
        HttpRoute httpRoute = new HttpRoute();
        String routeName = context.pathParam("routeName");
        System.out.println("12345679:"+routeName);
        List<HttpRoute> httpRoutes = RouteInfo.getRoute();
        HttpRoute target = null;
        if (httpRoutes == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(), routeName+"未找到",null);
        }
        System.out.println(httpRoutes.get(0));
        for (HttpRoute route : httpRoutes) {
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
    public MyResult<List<HttpRoute>> getAllRoutes(Context context) throws JsonProcessingException {
        List<HttpRoute> httpRoutes = RouteInfo.getRoute();
        if (httpRoutes == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(httpRoutes);
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
        return MyResult.success();
    }

    @Override
    public MyResult onOrOffline(String routeName ,Integer isPublish) throws JsonProcessingException {
        if (StringUtil.isNullOrEmpty(routeName) || isPublish == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由名和路由上下线设置均不可为空", null);
        }
        HttpRoute httpRoute = RouteInfo.getRoute(routeName);
        if (httpRoute == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        boolean flag = false;
        if (isPublish == 1) {
            flag = true;
        }
        httpRoute.setPublish(flag);
        RouteInfo.setRoute(httpRoute);
        return MyResult.success();
    }

}
