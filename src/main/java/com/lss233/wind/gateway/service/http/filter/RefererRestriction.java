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
 * @Date : 2022/5/1 18:15
 */
public class RefererRestriction extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(RefererRestriction.class);
   List<String> RefererBlackList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if(!refererRestriction(request)) {
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Referer is limited.".getBytes(StandardCharsets.UTF_8)))).addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    public boolean refererRestriction(HttpRequest request) {
        String referer = request.headers().get("Referer");
        LOG.debug(referer);
        RefererBlackList.add("http://127.0.0.1/");
        for (String regexp : RefererBlackList) {
            if (referer == null || referer.matches(regexp)) {
                return false;
            }
        }

        return true;
    }

    public List RefererBlackListAdd(String referer) {
        RefererBlackList.add(referer);
        return RefererBlackList;
    }
}
