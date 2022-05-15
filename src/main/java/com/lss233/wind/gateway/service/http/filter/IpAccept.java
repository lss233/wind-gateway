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
 * @Date : 2022/5/1 21:48
 */
public class IpAccept extends Filter implements PreHttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(IpAccept.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("Your IP address is not allowed.".getBytes(StandardCharsets.UTF_8)));
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

        String ip = request.headers().get("LocalAddr");
        if (ip != null && iPWhiteList.contains(ip)) {
            return true;
        }
        return false;
    }

    public List<String> IPWhiteListAdd(String routeName) throws JsonProcessingException {
        List<Filter> filters = new RouteServiceImpl().getRoute(routeName).getData().getFilters();
        Map<Object, Object> map = new HashMap<>();
        for (Filter filter : filters) {
            if (filter.getConfiguration().get("name").equals("IpAccept")) {
                map = filter.getConfiguration();
                break;
            }
        }
        iPWhiteList = (List<String>) map.get("blacklist");
        return iPWhiteList;
    }
}
