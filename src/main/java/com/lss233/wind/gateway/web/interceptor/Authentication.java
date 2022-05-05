package com.lss233.wind.gateway.web.interceptor;

import io.javalin.core.security.RouteRole;

import java.util.List;
import java.util.Set;

/**
 * @author zzl
 * @date 2022/5/5 16:37
 */
public class Authentication {
    public static boolean authenticate(Set<RouteRole> permittedUrl, Set<RouteRole> myUrl) {
        for (RouteRole routeRole : permittedUrl) {
            for (RouteRole role : myUrl) {
                if (routeRole.equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
