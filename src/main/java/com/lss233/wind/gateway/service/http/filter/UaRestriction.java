package com.lss233.wind.gateway.service.http.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.web.service.impl.RouteServiceImpl;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author : yjp
 * @Date : 2022/5/1 22:40
 */
public class UaRestriction extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UaRestriction.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("Not allowed.".getBytes(StandardCharsets.UTF_8)));
    List<String> UaBlackList = new ArrayList<>();

    public UaRestriction(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!uaRestriction(request)) {
                    LOG.info("Rate limit {} exceed!", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean uaRestriction (HttpRequest request) throws JsonProcessingException {

        String ua = request.headers().get("User-Agent");
        if (ua == null || UaBlackList.contains(ua)) {
            return false;
        }
        return true;
    }

    public List UaBlackListAdd(String routeName) throws JsonProcessingException {
        List<Filter> filters = new RouteServiceImpl().getRoute(routeName).getData().getFilters();
        Map<Object, Object> map = new HashMap<>();
        for (Filter filter : filters) {
            if (filter.getConfiguration().get("name").equals("UaRestriction")) {
                map = filter.getConfiguration();
                break;
            }
        }
        UaBlackList = (List<String>) map.get("blacklist");
        return UaBlackList;
    }
}
