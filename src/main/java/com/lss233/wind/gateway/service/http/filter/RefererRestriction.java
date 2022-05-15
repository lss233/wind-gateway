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
 * @Date : 2022/5/1 18:15
 */
public class RefererRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(RefererRestriction.class);
   List<String> RefererBlackList = new ArrayList<>();

    public RefererRestriction(String name) {
        super(name);
    }

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            try {
                if(!refererRestriction(request)) {
                    ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("Your referer host is not allowed".getBytes(StandardCharsets.UTF_8)))).addListener(ChannelFutureListener.CLOSE);
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean refererRestriction(HttpRequest request) throws JsonProcessingException {
        String referer = request.headers().get("Referer");
        LOG.debug(referer);
        for (String regexp : RefererBlackList) {
            if (referer == null || referer.matches(regexp)) {
                return false;
            }
        }

        return true;
    }

    public List<String> RefererBlackListAdd(String routeName) throws JsonProcessingException {
        List<Filter> filters = new RouteServiceImpl().getRoute(routeName).getData().getFilters();
        Map<Object, Object> map = new HashMap<>();
        for (Filter filter : filters) {
            if (filter.getConfiguration().get("name").equals("RefererRestriction")) {
                map = filter.getConfiguration();
                break;
            }
        }
        RefererBlackList = (List<String>) map.get("blacklist");

        return RefererBlackList;
    }
}
