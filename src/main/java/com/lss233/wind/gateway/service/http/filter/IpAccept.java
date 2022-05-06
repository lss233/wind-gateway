package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author : yjp
 * @Date : 2022/5/1 21:48
 */
public class IpAccept extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(IpAccept.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This IP is Accepted.".getBytes(StandardCharsets.UTF_8)));
    List<String> IPWhiteList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if (IpAccept(request)) {
                LOG.info("IP limit {}", ctx);
                ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    public boolean IpAccept(HttpRequest request) {
        String ip = request.headers().get("LocalAddr");
        IPWhiteList.add("192.168.29.1");
        if (ip != null && IPWhiteList.contains(ip)) {
            return true;
        }
        return false;
    }

    public List IPWhiteListAdd(String IP) {
        IPWhiteList.add(IP);
        return IPWhiteList;
    }
}
