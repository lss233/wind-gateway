package com.lss233.wind.gateway.web.entity;

import com.lss233.wind.gateway.common.Route;
import com.lss233.wind.gateway.service.http.HttpRoute;

import java.io.Serializable;

/**
 * @author zzl
 * @date 2022/5/10 19:38
 */
public class RouteView implements Serializable{

    public static final long serialVersionUID = 1L;

    Route route;
    HttpRoute httpRoute;

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public HttpRoute getHttpRoute() {
        return httpRoute;
    }

    public void setHttpRoute(HttpRoute httpRoute) {
        this.httpRoute = httpRoute;
    }
}
