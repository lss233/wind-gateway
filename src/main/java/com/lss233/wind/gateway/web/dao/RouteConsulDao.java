package com.lss233.wind.gateway.web.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.RouteInfo;

/**
 * @author zzl
 * @date 2022/5/5 20:29
 */
public class RouteConsulDao {

    public String storeRouteToConsul(Route route) throws JsonProcessingException {
        if (route == null) {
            throw new RuntimeException("route不能为空");
        }
        //序列化
        ObjectMapper objectMapper = new ObjectMapper();
        String routeJson = objectMapper.writeValueAsString(route);

        //存入consul
        RouteInfo.setRoute(route);

        return routeJson;
    }

}
