package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

public class DomainMatch extends MatchRule {
    private String domain;
    @Override
    boolean isMatch(HttpRequest request) {
        return domain.equalsIgnoreCase(request.headers().get("Host"));
    }
}
