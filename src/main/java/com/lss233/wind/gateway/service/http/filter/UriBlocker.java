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
 * @Date : 2022/5/1 23:07
 */
public class UriBlocker extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(UriBlocker.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Uri is Blockered.".getBytes(StandardCharsets.UTF_8)));
    List<String> uriBlackList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!UriBlock(request)) {
                    LOG.info("Rate limit {} exceed!", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean UriBlock(HttpRequest request) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();
        String uriBlackListJson = consulApi.getSingleKVForKey("UriBlackList");
        ObjectMapper objectMapper = new ObjectMapper();
        String[] list = objectMapper.readValue(uriBlackListJson, String[].class);
        uriBlackList = new ArrayList<>(Arrays.asList(list));
        String uri = request.headers().get("Uri");
        System.out.println(uri);
        System.out.println(request.headers().get("Uri"));
        if (uri == null || this.uriBlackList.contains(uri)) {
            return false;
        }

        return true;
    }

    public List<String> UriBlackListAdd(String Uri) throws JsonProcessingException {
        uriBlackList.add(Uri);
        ConsulApi consulApi = new ConsulApi();
        ObjectMapper objectMapper = new ObjectMapper();
        String uriBlackListJson = objectMapper.writeValueAsString(uriBlackList);
        consulApi.setKVValue("UriBlackList", uriBlackListJson);
        return uriBlackList;
    }
}
