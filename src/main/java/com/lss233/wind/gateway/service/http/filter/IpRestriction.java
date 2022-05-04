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
 * @Date : 2022/5/1 21:39
 */
public class IpRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(FlowLimitFilter.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This IP is limited.".getBytes(StandardCharsets.UTF_8)));

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(!IpRestriction((HttpRequest)msg)) {
            LOG.info("IP limit {}", ctx);
            ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean IpRestriction(HttpRequest request) {
        String ip = request.headers().get("LocalAddr");
        if (ip == null || ip.equals("192.168.29.1")) {

            return false;
        }
        System.out.println(ip);
        return true;
    }
}
