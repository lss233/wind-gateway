package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.http.HttpRoute;
import io.netty.util.internal.StringUtil;
import lombok.SneakyThrows;

import java.util.*;

/**
 * Author: icebigpig
 * Data: 2022/4/29 21:03
 * Version 1.0
 **/

public class RouteInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的route列表
     * @return List<HttpRoute>
     * @throws JsonProcessingException
     */
    public static List<HttpRoute> getRoute() throws JsonProcessingException {
        String valueResponse;
        HttpRoute[] httpRoutes;
        try{
            valueResponse = consulApi.getSingleKVForKey("routeList");
            ObjectMapper mapper = new ObjectMapper();
            // json 转数组对象
            httpRoutes = mapper.readValue(valueResponse, HttpRoute[].class);
        }catch (Exception e) {
//            e.printStackTrace();
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(httpRoutes));
    }

    /**
     * 将List<HttpRoute> 序列化并存储到consul中
     */
    public static void setRouteList(List<HttpRoute> routeList) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("routeList",mapper.writeValueAsString(routeList));
        System.out.println("setRouteList:"+routeList);
    }

    public static void updateRoteList(List<HttpRoute> updateRouteList) throws JsonProcessingException {
        consulApi.deleteKVValues("routeList");
        RouteInfo.setRouteList(updateRouteList);
    }

    /**
     * 通过路由名称获取单个路由
     */
    public static HttpRoute getRoute(String routeName) throws JsonProcessingException {

        List<HttpRoute> httpRoutes = RouteInfo.getRoute();
        if (httpRoutes == null) {
            return null;
        }
        for(HttpRoute httpRoute : httpRoutes){
            if(Objects.equals(httpRoute.getName(), routeName)){
                return httpRoute;
            }
        }
        return null;
    }

    /**
     * 通过已有路由名称修改路由，若不存在该路由名称，则进行追加路由
     */
    public static boolean setRoute(HttpRoute updateRoute) throws JsonProcessingException {

        // 修改前结果集
        List<HttpRoute> routeList = RouteInfo.getRoute();

        if (routeList == null) {
            routeList = new ArrayList<>();
        }

        // 待更新的结果集
        List<HttpRoute> updateRouteList = new ArrayList<>();

        // 如果存在该路由则获取到
        HttpRoute httpRoute = getRoute(updateRoute.getName());
        if (httpRoute == null){
            // 若不存在，则进行追加
            routeList.add(updateRoute);
            // TODO 将原先列表数据直接返回更新
            System.out.println("setRoute:##" + routeList);
            RouteInfo.updateRoteList(routeList);

        } else {
            // 若该路由信息存在，则进行更新
            for(HttpRoute routeItem : routeList){
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
     */
    public static boolean delRoute(String routeName) throws JsonProcessingException {

        // 修改前结果集
        List<HttpRoute> routeList = RouteInfo.getRoute();

        // 待更新的结果集
        List<HttpRoute> updateRouteList = new ArrayList<>();
        HttpRoute httpRoute = getRoute(routeName);
        if (httpRoute == null || routeList == null){
            // 若不存在，返回false
            return false;

        } else {
            // 若该路由信息存在，则进行更新
            for(HttpRoute routeItem : routeList){
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

    /**
     * 通过路由名关键词和路径关键词搜索符合条件的路由
     * @param routeName 路由名关键词
     * @param path  路径关键词
     * @return
     */
    public static List<HttpRoute> searchByNameAndPath(String routeName, String path) throws JsonProcessingException {
        List<HttpRoute> httpRoutes = getRoute();
        List<HttpRoute> targetList = new ArrayList<>();
        if (routeName == null) {
            routeName = "";
        }
        if (path == null) {
            path = "";
        }
        for (HttpRoute httpRoute : httpRoutes) {
            boolean flag = false;
            for (String s : httpRoute.getPath()) {
                if(s.contains(path)){
                    flag = true;
                    break;
                }
            }
            if (httpRoute.getName().contains(routeName) && flag) {
                targetList.add(httpRoute);
            }
        }
        return targetList;
    }

}
