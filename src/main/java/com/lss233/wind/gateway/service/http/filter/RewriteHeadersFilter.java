package com.lss233.wind.gateway.service.http.filter;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class RewriteHeadersFilter implements PreFilter {
    @Override
    public HttpObject onClientMessage(HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            request.headers().add("X-Request-Processed-By", "RewriteHeadersFilter");
        }
        return msg;
    }
}
