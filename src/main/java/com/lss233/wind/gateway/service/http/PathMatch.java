package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

import java.util.List;

/**
 * @Author : yjp
 * @Date : 2022/5/14 17:15
 */

public class PathMatch {

    private String value;

    List<PathMatch> pathMatch;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    boolean isMatch(HttpRequest request) {
        return request.uri().matches(getValue());
    }
}
