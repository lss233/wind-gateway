package com.lss233.wind.gateway.service.http;

import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author : yjp
 * @Date : 2022/5/14 17:15
 */
public class PathMatch extends MatchRule{
    @Override
    boolean isMatch(HttpRequest request) {
        return request.uri().matches(getValue());
    }
}
