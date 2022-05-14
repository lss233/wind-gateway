package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

public class DomainMatch extends MatchRule {
    @Override
    boolean isMatch(HttpRequest request) {
        return getValue().equalsIgnoreCase(request.headers().get("Host"));
    }
}