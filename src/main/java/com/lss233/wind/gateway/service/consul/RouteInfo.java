package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

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

    public static void updateRoteList(List<Route> updateRouteList) throws JsonProcessingException {
        consulApi.deleteKVValues("routeList");
        RouteInfo.setRouteList(updateRouteList);
    }

    /**
     * 通过路由名称获取单个路由
     * @param routeName
     * @return
     * @throws JsonProcessingException
     */
    public Route getRoute(String routeName) throws JsonProcessingException {

        List<Route> routes = RouteInfo.getRoute();

        for(Route route : routes){
            if(Objects.equals(route.getName(), routeName)){
                return route;
            }
        }
        return null;
    }

    /**
     * 通过已有路由名称修改路由，若不存在该路由名称，则进行追加路由
     */
    public boolean setRoute(Route updateRoute) throws JsonProcessingException {

        // 修改前结果集
        List<Route> routeList = RouteInfo.getRoute();

        // 待更新的结果集
        List<Route> updateRouteList = new ArrayList<>();

        // 如果存在该路由则获取到
        Route route = this.getRoute(updateRoute.getName());
        if (route == null){
            // 若不存在，则进行追加
            routeList.add(updateRoute);

            // TODO 将原先列表数据直接返回更新
            RouteInfo.updateRoteList(routeList);

        } else {
            // 若该路由信息存在，则进行更新
            for(Route routeItem : routeList){
                if(Objects.equals(routeItem.getName(), updateRoute.getName())){
                    updateRouteList.add(updateRoute);
                } else {
                    updateRouteList.add(routeItem);
                }
            }

            // TODO 将新的列表数据直接返回更新
            RouteInfo.updateRoteList(updateRouteList);
        }
        return true;
    }

    /**
     * 删除单个路由信息
     * 若返回值true则代表存在并且删除成功
     * 若返回false则表示不存在该路由信息
     * @param routeName
     * @throws JsonProcessingException
     */
    public boolean delRoute(String routeName) throws JsonProcessingException {

        // 修改前结果集
        List<Route> routeList = RouteInfo.getRoute();

        // 待更新的结果集
        List<Route> updateRouteList = new ArrayList<>();

        Route route = this.getRoute(routeName);
        if (route == null){
            // 若不存在，返回false
            return false;

        } else {
            // 若该路由信息存在，则进行更新
            for(Route routeItem : routeList){
                if(Objects.equals(routeItem.getName(), routeName)){
                    continue;
                } else {
                    updateRouteList.add(routeItem);
                }
            }

            // TODO 将新的列表数据直接返回更新
            RouteInfo.updateRoteList(updateRouteList);
        }
        return true;
    }
}
