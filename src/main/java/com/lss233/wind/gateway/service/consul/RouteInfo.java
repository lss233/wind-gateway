package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/4/29 21:03
 * Version 1.0
 **/

public class RouteInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的route列表
     * @return List<Route>
     * @throws JsonProcessingException
     */
    public static List<Route> getRoute() throws JsonProcessingException {
        String valueResponse = consulApi.getSingleKVForKey("routeList");
        ObjectMapper mapper = new ObjectMapper();
        // json 转数组对象
        Route[] routes = mapper.readValue(valueResponse, Route[].class);
        return new ArrayList<>(Arrays.asList(routes));
    }

    /**
     * 将List<Route> 序列化并存储到consul中
     * @param routeList
     * @throws JsonProcessingException
     */
    public static void setRouteList(List<Route> routeList) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("routeList",mapper.writeValueAsString(routeList));
    }

}
