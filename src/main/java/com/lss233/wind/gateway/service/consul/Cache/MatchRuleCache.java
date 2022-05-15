package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.consul.MatchRuleInfo;
import com.lss233.wind.gateway.service.http.MatchRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/5/15 9:20
 * Version 1.0
 **/

public class MatchRuleCache {

    private static List<MatchRule> matchRules = new ArrayList<>();

    public static void updateCache() throws JsonProcessingException {
        matchRules = MatchRuleInfo.getMatchRule();
    }

    public static List<MatchRule> getMatchRules(){
        return MatchRuleCache.matchRules;
    }

}
