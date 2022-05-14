package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.consul.RouteInfo;
import com.lss233.wind.gateway.service.http.HttpRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/5/14 21:24
 * Version 1.0
 **/

public class HttpRouteCache {

    private static List<HttpRoute> routes = new ArrayList<>();

    public static void updateCache() throws JsonProcessingException {
        HttpRouteCache.routes = RouteInfo.getRoute();
    }

    public static List<HttpRoute> getHttpRoutes(){
        return HttpRouteCache.routes;
    }

}
