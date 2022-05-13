package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

public abstract class MatchRule {
    abstract boolean isMatch(HttpRequest request);
}
