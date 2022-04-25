package com.lss233.wind.gateway.service.http.filter;

import io.netty.handler.codec.http.HttpObject;

public interface PostFilter {
    HttpObject onServerMessage(HttpObject object);
}
