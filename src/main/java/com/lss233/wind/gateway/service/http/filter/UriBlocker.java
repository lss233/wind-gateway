package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * @Author : yjp
 * @Date : 2022/5/1 23:07
 */
public class UriBlocker extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(FlowLimitFilter.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Uri is Blockered.".getBytes(StandardCharsets.UTF_8)));

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
                if(UriBlocker((HttpRequest)msg)) {
                    LOG.info("Rate limit {} exceed!", ctx);
                    ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean UriBlocker(HttpRequest request) {
        String uri = request.uri();
        if (uri == null || uri.equals("/home")) {
            return false;
        }
        return true;
    }
}
