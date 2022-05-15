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
 * @Date : 2022/5/1 23:07
 */
public class UriBlocker extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(UriBlocker.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("access is not allowed.".getBytes(StandardCharsets.UTF_8)));
    List<String> uriBlackList = new ArrayList<>();

    public UriBlocker(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!uriBlock(request)) {
                    LOG.info("Rate limit {} exceed!", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }
    public boolean uriBlock(HttpRequest request) throws JsonProcessingException {

        String uri = request.headers().get("Uri");
        System.out.println(request.headers().get("Uri"));
        if (uri == null || this.uriBlackList.contains(uri)) {
            return false;
        }

        return true;
    }

    public List<String> UriBlackListAdd(String routeName) throws JsonProcessingException {
        List<Filter> filters = new RouteServiceImpl().getRoute(routeName).getData().getFilters();
        Map<Object, Object> map = new HashMap<>();
        for (Filter filter : filters) {
            if (filter.getConfiguration().get("name").equals("UriBlocker")) {
                map = filter.getConfiguration();
                break;
            }
        }
        uriBlackList = (List<String>) map.get("blacklist");
        return uriBlackList;
    }
}
