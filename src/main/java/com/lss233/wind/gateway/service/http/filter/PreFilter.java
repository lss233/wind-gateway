package com.lss233.wind.gateway.service.http.filter;

import io.netty.handler.codec.http.HttpObject;

public interface PreFilter {
    HttpObject onClientMessage(HttpObject msg);
}
