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
 * @Date : 2022/5/1 23:07
 */
public class UriBlocker extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(UriBlocker.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, Unpooled.wrappedBuffer("This Uri is Blockered.".getBytes(StandardCharsets.UTF_8)));
    List<String> UriBlackList = new ArrayList<>();

    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;
            if(!UriBlocker(request)) {
                LOG.info("Rate limit {} exceed!", ctx);
                ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    public boolean UriBlocker(HttpRequest request) {
        String uri = request.uri();
        System.out.println(uri);
        UriBlackList.add("/sugrec?prod=pc_his&from=pc_web&json=1&sid=36309_31253_36167_34584_35979_36073_36337_26350_36300_36312_36061&hisdata=&_t=1651818115692&req=2&csor=0");
        if (uri == null || UriBlackList.contains(uri)) {
            return false;
        }
        return true;
    }

    public List UriBlackListAdd(String Uri) {
        UriBlackList.add(Uri);
        return UriBlackList;
    }
}
