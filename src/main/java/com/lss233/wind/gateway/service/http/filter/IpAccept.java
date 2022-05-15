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
 * @Date : 2022/5/1 21:48
 */
public class IpAccept extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(IpAccept.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This IP is Accepted.".getBytes(StandardCharsets.UTF_8)));
    List<String> iPWhiteList = new ArrayList<>();

    public IpAccept(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if (ipAccept(request)) {
                    LOG.info("IP limit {}", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean ipAccept(HttpRequest request) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();
        String ipWhiteListJson = consulApi.getSingleKVForKey("iPWhiteList" + getRoute().getName());
        ObjectMapper objectMapper = new ObjectMapper();
        String[] list = objectMapper.readValue(ipWhiteListJson, String[].class);
        iPWhiteList = new ArrayList<>(Arrays.asList(list));
        String ip = request.headers().get("LocalAddr");
        if (ip != null && iPWhiteList.contains(ip)) {
            return true;
        }
        return false;
    }

    public List<String> IPWhiteListAdd(String IP) throws JsonProcessingException {
        iPWhiteList.add(IP);
        ConsulApi consulApi = new ConsulApi();
        ObjectMapper objectMapper = new ObjectMapper();
        String ipWhiteListJson = objectMapper.writeValueAsString(iPWhiteList);
        consulApi.setKVValue("iPWhiteList" + getRoute().getName(), ipWhiteListJson);
        return iPWhiteList;
    }
}
