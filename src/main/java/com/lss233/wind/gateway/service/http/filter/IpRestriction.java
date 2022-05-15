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
 * @Date : 2022/5/1 21:39
 */
public class IpRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(IpRestriction.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("Your IP address is not allowed".getBytes(StandardCharsets.UTF_8)));
    List<String> ipBlackList = new ArrayList<>();

    public IpRestriction(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if (!ipRestriction(request)) {
                    LOG.info("IP limit {}", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean ipRestriction(HttpRequest request) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();
        String ipBlackListJson = consulApi.getSingleKVForKey("IpBlackList" + getRoute().getName());
        ObjectMapper objectMapper = new ObjectMapper();
        String[] list = objectMapper.readValue(ipBlackListJson, String[].class);
        ipBlackList = new ArrayList<>(Arrays.asList(list));
        String ip = request.headers().get("LocalAddr");
        if (ip == null || ipBlackList.contains(ip)) {
            return false;
        }
        return true;
    }

    public List<String> IpBlackListAdd(String IP) throws JsonProcessingException {
        ipBlackList.add(IP);
        ConsulApi consulApi = new ConsulApi();
        ObjectMapper objectMapper = new ObjectMapper();
        String ipBlackListJson = objectMapper.writeValueAsString(ipBlackList);
        consulApi.setKVValue("IpBlackList" + getRoute().getName(), ipBlackListJson);
        return ipBlackList;
    }
}
