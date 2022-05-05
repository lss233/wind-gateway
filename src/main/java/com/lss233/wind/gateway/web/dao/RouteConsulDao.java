package com.lss233.wind.gateway.web.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.service.consul.entity.KeyValue;

/**
 * @author zzl
 * @date 2022/5/5 20:29
 */
public class RouteConsulDao {

    public String storeRouteToConsul(Route route) throws JsonProcessingException {
        ConsulApi api = new ConsulApi();
        //序列化
        ObjectMapper objectMapper = new ObjectMapper();
        String routeJson = objectMapper.writeValueAsString(route);
        KeyValue keyValue = new KeyValue();
        //将路由名写入keyValue中作为键
        keyValue.setKey(route.getName());
        //将整个route对象系列化后作为value写入keyValue
        keyValue.setValue(routeJson);
        //将keyValue存入consul中
        api.setKVValue(keyValue);

        return routeJson;
    }
}
