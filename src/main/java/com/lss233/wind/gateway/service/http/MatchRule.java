package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

public abstract class MatchRule {
    private String value;

    abstract boolean isMatch(HttpRequest request);

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
