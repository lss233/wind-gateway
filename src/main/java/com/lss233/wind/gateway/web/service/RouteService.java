package com.lss233.wind.gateway.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.http.HttpRoute;
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
     * @param httpRoute 路由对象
     * @return
     */
    MyResult setRoute(HttpRoute httpRoute);

    /**
     * 获取路由信息
     * @param routeName 路由名
     * @return
     */
    MyResult<HttpRoute> getRoute(String routeName);

    /**
     * 用于展示路由列表
     * @return
     */
    MyResult<List<HttpRoute>> getAllRoutes();

    /**
     * 删除指定路由
     * @param routeName 路由名
     * @return
     */
    MyResult deleteRoute(String routeName);

    /**
     * 设置路由的上下线
     * @param isPublish 是否上线
     * @return
     */
    MyResult onOrOffline(String routeName, Integer isPublish);

    /**
     * 通过路由名关键词和路径关键词搜索符合条件的路由
     * @param routeName 路由名关键词
     * @param path  路径关键词
     * @return
     */
    MyResult search(String routeName, String path);
}
