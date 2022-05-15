package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.consul.PathMatchInfo;
import com.lss233.wind.gateway.service.http.PathMatch;

/**
 * Author: icebigpig
 * Data: 2022/5/15 9:20
 * Version 1.0
 **/

public class PathMatchCache {

    private static PathMatch pathMatch = new PathMatch();

    public static void updateCache() throws JsonProcessingException {
        pathMatch = PathMatchInfo.getMatchRule();
    }

    public static PathMatch getPathMatch(){
        return PathMatchCache.pathMatch;
    }

}
