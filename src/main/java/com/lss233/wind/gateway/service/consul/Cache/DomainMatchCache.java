package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.service.consul.DomainMatchInfo;
import com.lss233.wind.gateway.service.consul.PathMatchInfo;
import com.lss233.wind.gateway.service.http.DomainMatch;
import com.lss233.wind.gateway.service.http.PathMatch;

/**
 * Author: icebigpig
 * Data: 2022/5/15 9:20
 * Version 1.0
 **/

public class DomainMatchCache {

    private static DomainMatch domainMatch = new DomainMatch();

    public static void updateCache() throws JsonProcessingException {
        domainMatch = DomainMatchInfo.getDomainMatch();
    }

    public static DomainMatch getPathMatch(){
        return DomainMatchCache.domainMatch;
    }

}
