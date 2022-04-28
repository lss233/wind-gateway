package com.lss233.wind.gateway.service.http.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;

public interface PreHttpFilter {
    void onClientMessage(ChannelHandlerContext ctx, HttpObject msg);
}
