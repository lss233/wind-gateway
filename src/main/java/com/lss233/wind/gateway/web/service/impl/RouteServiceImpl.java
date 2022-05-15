package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.web.dao.RouteConsulDao;
import com.lss233.wind.gateway.web.service.PluginService;
import com.lss233.wind.gateway.web.service.RouteService;
import com.lss233.wind.gateway.web.util.FilterConvertor;
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

    @Override
    public MyResult<HttpRoute> setRoute(HttpRoute httpRoute) {
        if (httpRoute == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(),"路由配置失败，路由信息不可为null", null);
        }
        try {
            FilterConvertor.setPlugin(httpRoute);
            RouteInfo.setRoute(httpRoute);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR.getCode(),"插件启用错误",null);
        }
        return MyResult.success(httpRoute);
    }

    @Override
    public MyResult<HttpRoute> getRoute(String routeName) {
        HttpRoute httpRoute = null;
        try {
            httpRoute = RouteInfo.getRoute(routeName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (httpRoute == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(), routeName+"未找到",null);
        }
        return MyResult.success(httpRoute);
    }

    @Override
    public MyResult<List<HttpRoute>> getAllRoutes() {
        List<HttpRoute> httpRoutes = null;
        try {
            httpRoutes = RouteInfo.getRoute();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (httpRoutes == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(httpRoutes);
    }

    @Override
    public MyResult deleteRoute(String routeName) {
        if (StringUtil.isNullOrEmpty(routeName)) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(),"routeName不可为空",null);
        }
        boolean isDel = false;
        try {
            isDel = RouteInfo.delRoute(routeName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (!isDel){
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由删除失败",null);
        }
        return MyResult.success();
    }

    @Override
    public MyResult onOrOffline(String routeName) {
        if (StringUtil.isNullOrEmpty(routeName)) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由名和路由上下线设置均不可为空", null);
        }
        try{
            HttpRoute httpRoute = RouteInfo.getRoute(routeName);
            if (httpRoute == null) {
                return MyResult.fail(ResultEnum.NOT_FOUND);
            }
            httpRoute.setPublish(!httpRoute.isPublish());
            RouteInfo.setRoute(httpRoute);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }

        return MyResult.success();
    }

    @Override
    public MyResult search(String routeName, String path) {
        List<HttpRoute> httpRoutes;
        try {
            httpRoutes = RouteInfo.searchByNameAndPath(routeName, path);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (httpRoutes.isEmpty()) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(httpRoutes);
    }

}
