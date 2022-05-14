package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Author : yjp
 * @Date : 2022/5/7 17:28
 */
public class CORS extends Filter implements PreHttpFilter{
    public CORS(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            request.headers().add("Access-Control-Allow-Origin", "*");
            request.headers().add("Access-Control-Allow-Methods", "*");
            request.headers().add("Access-Control-Allow-Headers", "*");
        }
    }
}
