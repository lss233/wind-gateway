package com.lss233.wind.gateway.service.http.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;

public interface PostHttpFilter {
    HttpObject onServerMessage(ChannelHandlerContext ctx, HttpObject object);
}
