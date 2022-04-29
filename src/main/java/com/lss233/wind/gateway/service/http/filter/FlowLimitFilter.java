package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.http.HttpForwardFrontendHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.nio.charset.StandardCharsets;

/**
 * @Author : yjp
 * @Date : 2022/4/28 21:01
 */
public class FlowLimitFilter extends Filter implements PreHttpFilter{
    private static final Logger LOG = LoggerFactory.getLogger(FlowLimitFilter.class);
    private final static DefaultFullHttpResponse RESPONSE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TOO_MANY_REQUESTS, Unpooled.wrappedBuffer("Too many requests.".getBytes(StandardCharsets.UTF_8)));
    @Override
    public void onClientMessage(ChannelHandlerContext ctx, HttpObject msg) {
        if(!rateLimiterByZset("1", 5, 1)) {
            LOG.info("Rate limit {} exceed!", ctx);
            ctx.writeAndFlush(RESPONSE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    public static boolean rateLimiterByZset(String key,int maxCount,int timeRange){
        JedisPool jedisPool = new JedisPool();
        try (Jedis jedis = jedisPool.getResource()) {
            long currentTime = System.currentTimeMillis();        //当前时间戳
            long secondTime = currentTime-timeRange* 1000L;      //second秒前的时间戳
            long memberCount = jedis.zcount(key,secondTime,currentTime);
            if(memberCount >= maxCount){
                return false;
            }
            jedis.zadd(key,currentTime,currentTime+"");
            //删除时间框外的数据，因为它们已经没有用了
            jedis.zremrangeByScore(key,0,secondTime);
        }
        return true;
    }
}
