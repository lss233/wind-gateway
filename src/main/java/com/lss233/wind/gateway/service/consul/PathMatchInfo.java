package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.http.PathMatch;

/**
 * Author: icebigpig
 * Data: 2022/5/15 8:59
 * Version 1.0
 **/

public class PathMatchInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的PathMatch
     */
    public static PathMatch getMatchRule() throws JsonProcessingException {
        String valueResponse;
        PathMatch pathMatch;
        try{
            valueResponse = consulApi.getSingleKVForKey("PathMatch");
            ObjectMapper mapper = new ObjectMapper();
            // json 转数组对象
            pathMatch = mapper.readValue(valueResponse, PathMatch.class);
        }catch (Exception e) {
            return null;
        }
        return pathMatch;
    }

    /**
     * 将 PathMatch 序列化并存储到consul中
     */
    public static void setPathMatch(PathMatch pathMatch) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("PathMatch",mapper.writeValueAsString(pathMatch));
        System.out.println("setPathMatch:"+pathMatch);
    }

    /**
     * 更新consul中存储的json数据
     */
    public static void updateMatchRuleList(PathMatch updatePathMatch) throws JsonProcessingException {
        consulApi.deleteKVValues("MatchRule");
        PathMatchInfo.setPathMatch(updatePathMatch);
    }

}
