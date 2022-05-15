package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.http.MatchRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Author: icebigpig
 * Data: 2022/5/15 8:59
 * Version 1.0
 **/

public class MatchRuleInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的MatchRule列表
     * @return List<MatchRule>
     */
    public static List<MatchRule> getMatchRule() throws JsonProcessingException {
        String valueResponse;
        MatchRule[] matchRules;
        try{
            valueResponse = consulApi.getSingleKVForKey("MatchRule");
            ObjectMapper mapper = new ObjectMapper();
            // json 转数组对象
            matchRules = mapper.readValue(valueResponse, MatchRule[].class);
        }catch (Exception e) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(matchRules));
    }

    /**
     * 将List<MatchRule> 序列化并存储到consul中
     */
    public static void setMatchRuleList(List<MatchRule> matchRuleList) throws JsonProcessingException {
        //序列化
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("MatchRule",mapper.writeValueAsString(matchRuleList));
        System.out.println("setMatchRuleList:"+matchRuleList);
    }

    /**
     * 更新consul中存储的json数据
     */
    public static void updateMatchRuleList(List<MatchRule> updateMatchRuleList) throws JsonProcessingException {
        consulApi.deleteKVValues("MatchRule");
        MatchRuleInfo.setMatchRuleList(updateMatchRuleList);
    }

    /**
     * 通过MatchRuleValue获取单个MatchRule
     */
    public static MatchRule getMatchRule(String matchRuleValue) throws JsonProcessingException {

        List<MatchRule> matchRules = MatchRuleInfo.getMatchRule();
        if (matchRules == null) {
            return null;
        }
        for(MatchRule matchRule : matchRules){
            if(Objects.equals(matchRule.getValue(), matchRuleValue)){
                return matchRule;
            }
        }
        return null;
    }

    /**
     * 通过已有MatchRuleValue修改MatchRule，若不存在，则进行追加路由
     */
    public static boolean setMatchRule(MatchRule updateMatchRule) throws JsonProcessingException {

        // 修改前结果集
        List<MatchRule> matchRuleList = MatchRuleInfo.getMatchRule();

        if (matchRuleList == null) {
            matchRuleList = new ArrayList<>();
        }

        // 待更新的结果集
        List<MatchRule> updateMatchRuleList = new ArrayList<>();

        // 如果存在该MatchRule则获取到
        MatchRule matchRule = getMatchRule(updateMatchRule.getValue());
        if (matchRule == null){
            // 若不存在，则进行追加
            matchRuleList.add(updateMatchRule);
            // TODO 将原先列表数据直接返回更新
            System.out.println("setMatchRule:##" + matchRuleList);
            MatchRuleInfo.updateMatchRuleList(matchRuleList);

        } else {
            // 若该MatchRule信息存在，则进行更新
            for(MatchRule matchRuleItem : matchRuleList){
                if(Objects.equals(matchRuleItem.getValue(), updateMatchRule.getValue())){
                    updateMatchRuleList.add(updateMatchRule);
                } else {
                    updateMatchRuleList.add(matchRuleItem);
                }
            }

            // TODO 将新的列表数据直接返回更新
            MatchRuleInfo.updateMatchRuleList(updateMatchRuleList);
        }
        return true;
    }

    /**
     * 删除单个MatchRule信息
     * 若返回值true则代表存在并且删除成功
     * 若返回false则表示不存在该MatchRule信息
     */
    public static boolean delMatchRule(String matchRuleValue) throws JsonProcessingException {

        // 修改前结果集
        List<MatchRule> matchRuleList = MatchRuleInfo.getMatchRule();

        // 待更新的结果集
        List<MatchRule> updateMatchRuleList = new ArrayList<>();
        MatchRule matchRule = getMatchRule(matchRuleValue);
        if (matchRule == null || matchRuleList == null){
            // 若不存在，返回false
            return false;

        } else {
            // 若该MatchRule信息存在，则进行更新
            for(MatchRule matchRuleItem : matchRuleList){
                if(Objects.equals(matchRuleItem.getValue(), matchRuleValue)){
                    continue;
                } else {
                    updateMatchRuleList.add(matchRuleItem);
                }
            }

            // TODO 将新的列表数据直接返回更新
            MatchRuleInfo.updateMatchRuleList(updateMatchRuleList);
        }
        return true;
    }



}
