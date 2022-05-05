package com.lss233.wind.gateway.web.entity;

import io.javalin.core.security.RouteRole;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zzl
 * @date 2022/5/4 8:30
 */
public class User implements Serializable {

    public static long serializableID = 1L;

    private String username;
    private String password;
    private Set<RouteRole> myUrls = new LinkedHashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RouteRole> getMyUrls() {
        return myUrls;
    }

    public void setMyUrls(Set<RouteRole> myUrls) {
        this.myUrls = myUrls;
    }
}
