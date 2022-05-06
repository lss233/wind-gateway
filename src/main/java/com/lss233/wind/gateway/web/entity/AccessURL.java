package com.lss233.wind.gateway.web.entity;

import io.javalin.core.security.RouteRole;

/**
 * 刚开始没有理解做的什么安全认证，以为是微服务的授权，因此对权限进行了分级，用路径名作为权限并当作javalin的角色使用，
 * 来进行访问授权管理。但由于负责的是网页网关控制台的安全认证，且只有管理员使用，无需分级，故修改为两个角色，命名等为了方便暂时便不做修改。
 * @author zzl
 * @date 2022/5/5 16:20
 */
public enum AccessURL implements RouteRole {
    //各个api的url
    ADMIN,
    ANYONE,
//    LOGOUT,
//    ADD_ROUTE,
//    GET_ROUTE
}
