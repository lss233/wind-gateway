package com.lss233.wind.gateway.web.entity;

import io.javalin.core.security.RouteRole;

/**
 * @author zzl
 * @date 2022/5/5 16:20
 */
public enum AccessURL implements RouteRole {
    //各个api的url
    ADMIN,
    ANYONE,
    LOGOUT,
    ADD_ROUTE,
    GET_ROUTE
}
