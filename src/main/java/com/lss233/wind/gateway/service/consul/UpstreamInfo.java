package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Upstream;

import java.util.ArrayList;
import java.util.Arrays;
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
        String valueResponse = consulApi.getSingleKVForKey("upstreamList");
        ObjectMapper mapper = new ObjectMapper();
        System.out.println("读取到的数据："+valueResponse);
        // json 转数组对象
        Upstream[] upstreams = mapper.readValue(valueResponse, Upstream[].class);
        return new ArrayList<>(Arrays.asList(upstreams));
    }

    /**
     * 将List<Upstream> 序列化并存储到consul中
     * @param upstreamList
     * @throws JsonProcessingException
     */
    public static void setUpstreamList(List<Upstream> upstreamList) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("upstreamList",mapper.writeValueAsString(upstreamList));
    }

}
