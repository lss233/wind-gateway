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
 * @Date : 2022/5/1 18:15
 */
public class RefererRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(FlowLimitFilter.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Referer is limited.".getBytes(StandardCharsets.UTF_8)));

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(refererRestriction((HttpRequest)msg)) {
            LOG.info("Rate limit {} exceed!", ctx);
            ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean refererRestriction(HttpRequest request) {
        String referer = request.headers().get("Referer");
        if (referer == null || !referer.startsWith("http://127.0.0.1/")) {
            return false;
        }
        return true;
    }
}
