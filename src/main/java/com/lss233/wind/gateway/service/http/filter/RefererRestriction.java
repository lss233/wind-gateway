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
 * @Date : 2022/5/1 18:15
 */
public class RefererRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(RefererRestriction.class);
   List<String> RefererBlackList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!refererRestriction(request)) {
                    ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Referer is limited.".getBytes(StandardCharsets.UTF_8)))).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean refererRestriction(HttpRequest request) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();
        String refererBlackListJson = consulApi.getSingleKVForKey("RefererBlackList");
        ObjectMapper objectMapper = new ObjectMapper();
        String[] list = objectMapper.readValue(refererBlackListJson, String[].class);
        RefererBlackList = new ArrayList<>(Arrays.asList(list));
        String referer = request.headers().get("Referer");
        LOG.debug(referer);
        for (String regexp : RefererBlackList) {
            if (referer == null || referer.matches(regexp)) {
                return false;
            }
        }

        return true;
    }

    public List<String> RefererBlackListAdd(String referer) throws JsonProcessingException {
        RefererBlackList.add(referer);
        ConsulApi consulApi = new ConsulApi();
        ObjectMapper objectMapper = new ObjectMapper();
        String RefererBlackListJson = objectMapper.writeValueAsString(RefererBlackList);
        consulApi.setKVValue("RefererBlackList", RefererBlackListJson);
        return RefererBlackList;
    }
}
