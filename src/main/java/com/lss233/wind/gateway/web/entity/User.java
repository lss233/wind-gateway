package com.lss233.wind.gateway.web.entity;

import javax.management.relation.Role;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zzl
 * @date 2022/5/4 8:30
 */
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    private String username = "";
    private String password = "";
    private Set<AccessURL> myUrls = new HashSet<>();

    public User() {
        myUrls.add(AccessURL.ANYONE);
    }

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

    public Set<AccessURL> getMyUrls() {
        return myUrls;
    }

    public void setMyUrls(Set<AccessURL> myUrls) {
        this.myUrls = myUrls;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", myUrls=" + myUrls +
                '}';
    }
}
