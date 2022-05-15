package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

import java.util.List;

public class DomainMatch {

    private String value;

    List<DomainMatch> DomainMath;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    boolean isMatch(HttpRequest request) {
        return getValue().equalsIgnoreCase(request.headers().get("Host"));
    }
}
