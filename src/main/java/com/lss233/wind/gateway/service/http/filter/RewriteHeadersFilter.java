package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public class RewriteHeadersFilter extends Filter implements PreHttpFilter {
    public RewriteHeadersFilter(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            request.headers().add("X-Request-Processed-By", "RewriteHeadersFilter");
            request.headers().remove("Host");
            request.headers().add("Host", "www.baidu.com");
        }
    }
}
