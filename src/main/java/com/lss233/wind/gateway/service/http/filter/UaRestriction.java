package com.lss233.wind.gateway.service.http.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : yjp
 * @Date : 2022/5/1 22:40
 */
public class UaRestriction extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(UaRestriction.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This User-Agent is limited.".getBytes(StandardCharsets.UTF_8)));
    List<String> UaBlackList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!UaRestriction(request)) {
                    LOG.info("Rate limit {} exceed!", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean UaRestriction (HttpRequest request) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();
        String UaBlackListJson = consulApi.getSingleKVForKey("UaBlackList");
        ObjectMapper objectMapper = new ObjectMapper();
        String[] list = objectMapper.readValue(UaBlackListJson, String[].class);
        UaBlackList = new ArrayList<>(Arrays.asList(list));
        String ua = request.headers().get("User-Agent");
        if (ua == null || UaBlackList.contains(ua)) {
            return false;
        }
        return true;
    }

    public List UaBlackListAdd(String Ua) throws JsonProcessingException {
        UaBlackList.add(Ua);
        ConsulApi consulApi = new ConsulApi();
        ObjectMapper objectMapper = new ObjectMapper();
        String UaBlackListJson = objectMapper.writeValueAsString(UaBlackList);
        consulApi.setKVValue("UaBlackList", UaBlackListJson);
        return UaBlackList;
    }
}
