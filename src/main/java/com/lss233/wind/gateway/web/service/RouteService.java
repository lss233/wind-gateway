package com.lss233.wind.gateway.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.web.util.MyResult;
import io.javalin.http.Context;

import java.util.List;

/**
 * @author zzl
 * @date 2022/5/5 20:34
 */
public interface RouteService {
    /**
     * 修改和创建路由
     * @param context
     * @return
     */
    MyResult setRoute(Context context) throws JsonProcessingException;

    /**
     * 获取路由信息
     * @param context
     * @return
     */
    MyResult<Route> getRoute(Context context) throws JsonProcessingException;

    /**
     * 用于展示路由列表
     * @return
     */
    MyResult<List<Route>> getAllRoutes(Context context) throws JsonProcessingException;

    /**
     * 删除指定路由
     * @param routeName 路由名
     * @return
     */
    MyResult deleteRoute(String routeName) throws JsonProcessingException;

    /**
     * 设置路由的上下线
     * @param isPublish 是否上线
     * @return
     */
    MyResult onOrOffline(String routeName, Integer isPublish) throws JsonProcessingException;
}
