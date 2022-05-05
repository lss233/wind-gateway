package com.lss233.wind.gateway.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;

/**
 * @author zzl
 * @date 2022/5/5 20:34
 */
public interface RouteService {
    /**
     * 创建路由
     * @param context
     */
    void createRoute(Context context) throws JsonProcessingException;

    /**
     * 获取路由信息
     * @param context
     */
    void getRoute(Context context);
}
