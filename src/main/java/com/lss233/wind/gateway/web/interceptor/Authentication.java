package com.lss233.wind.gateway.web.interceptor;

import com.lss233.wind.gateway.web.entity.AccessURL;
import io.javalin.core.security.RouteRole;

import java.util.Set;

/**
 * 用于权限分级时的鉴权，在端口管理中被使用。
 * @author zzl
 * @date 2022/5/5 16:37
 */
public class Authentication {
    public static boolean authenticate(Set<RouteRole> permittedUrl, Set myUrl) {
        for (RouteRole routeRole : permittedUrl) {
            for (Object role : myUrl) {
                if (routeRole.toString().equals(role.toString())) {
                    return true;
                }
            }
        }
        return false;
    }
}
