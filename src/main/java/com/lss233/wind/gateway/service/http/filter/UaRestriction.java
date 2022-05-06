package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @Author : yjp
 * @Date : 2022/5/1 22:40
 */
public class UaRestriction extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(FlowLimitFilter.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Referer is limited.".getBytes(StandardCharsets.UTF_8)));

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(UaRestriction((HttpRequest)msg)) {
            LOG.info("Rate limit {} exceed!", ctx);
            ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean UaRestriction (HttpRequest request) {
        String ua = request.headers().get("User-Agent");
        if (ua == null || ua.equals("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36 Edg/100.0.1185.50")) {
            return false;
        }
        return true;
    }
}
