package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.http.DomainMatch;
import com.lss233.wind.gateway.service.http.PathMatch;

/**
 * Author: icebigpig
 * Data: 2022/5/15 8:59
 * Version 1.0
 **/

public class DomainMatchInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的 DomainMatch
     */
    public static DomainMatch getDomainMatch() throws JsonProcessingException {
        String valueResponse;
        DomainMatch domainMatch;
        try{
            valueResponse = consulApi.getSingleKVForKey("DomainMatch");
            ObjectMapper mapper = new ObjectMapper();
            // json 转数组对象
            domainMatch = mapper.readValue(valueResponse, DomainMatch.class);
        }catch (Exception e) {
            return null;
        }
        return domainMatch;
    }

    /**
     * 将 DomainMatch 序列化并存储到consul中
     */
    public static void setDomainMatch(DomainMatch domainMatch) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("DomainMatch",mapper.writeValueAsString(domainMatch));
        System.out.println("setDomainMatch:"+domainMatch);
    }

    /**
     * 更新consul中存储的json数据
     */
    public static void updateDomainMatch(DomainMatch updateDomainMatch) throws JsonProcessingException {
        consulApi.deleteKVValues("MatchRule");
        DomainMatchInfo.setDomainMatch(updateDomainMatch);
    }

}
