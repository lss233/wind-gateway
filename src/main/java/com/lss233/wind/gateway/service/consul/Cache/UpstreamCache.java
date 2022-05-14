package com.lss233.wind.gateway.service.consul.Cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.UpstreamInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/5/14 22:02
 * Version 1.0
 **/

public class UpstreamCache {

    private static List<Upstream> upstreams = new ArrayList<>();

    public static void updateCache() throws JsonProcessingException {
        UpstreamCache.upstreams = UpstreamInfo.getUpstreams();
    }

    public static List<Upstream> getUpstream() {
        return UpstreamCache.upstreams;
    }
}
