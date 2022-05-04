package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.entity.UpstreamConvert;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/5/1 15:25
 * Version 1.0
 **/

public class UpstreamInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的upstream列表
     * @return List<Upstream>
     * @throws JsonProcessingException
     */
    public static List<Upstream> getUpstream() throws JsonProcessingException {

        List<Upstream> upstreamList = new ArrayList<>();
        String valueResponse = consulApi.getSingleKVForKey("upstreamList");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("读取到的数据："+valueResponse);
        // json 转数组对象
        UpstreamConvert[] upstreamConverts = mapper.readValue(valueResponse, UpstreamConvert[].class);

        for(UpstreamConvert upstreamConvert : upstreamConverts){
            Upstream upstream = new Upstream();
            upstream.setName(upstreamConvert.getName());
            upstream.setDescription(upstreamConvert.getDescription());
            upstream.setEndpoints( upstreamConvert.getEndpoints());
            upstream.setRetryAttempts(upstreamConvert.getRetryAttempts());
            upstream.setRetryTimeout(upstreamConvert.getRetryTimeout());
            upstream.setConnectTimeout(upstreamConvert.getConnectTimeout());
            upstream.setSendTimeout(upstreamConvert.getSendTimeout());
            upstream.setReceiveTimeout(upstreamConvert.getReceiveTimeout());
            upstream.setScheme(upstreamConvert.getScheme());
            upstreamList.add(upstream);
        }
        return upstreamList;
    }

    /**
     * 将List<Upstream> 序列化并存储到consul中
     * @param upstreamList
     * @throws JsonProcessingException
     */
    public static void setUpstreamList(List<Upstream> upstreamList) throws JsonProcessingException {
        //序列化
        List<UpstreamConvert> upstreamConvertList = new ArrayList<>();
        for(Upstream upstream : upstreamList){
            UpstreamConvert upstreamConvert = new UpstreamConvert();

            upstreamConvert.setName(upstream.getName());
            upstreamConvert.setDescription(upstream.getDescription());
            upstreamConvert.setEndpoints( upstream.getEndpoints());
            upstreamConvert.setRetryAttempts(upstream.getRetryAttempts());
            upstreamConvert.setRetryTimeout(upstream.getRetryTimeout());
            upstreamConvert.setConnectTimeout(upstream.getConnectTimeout());
            upstreamConvert.setSendTimeout(upstream.getSendTimeout());
            upstreamConvert.setReceiveTimeout(upstream.getReceiveTimeout());
            upstreamConvert.setScheme(upstream.getScheme());

            upstreamConvertList.add(upstreamConvert);
        }

        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("upstreamList",mapper.writeValueAsString(upstreamConvertList));
    }

}
