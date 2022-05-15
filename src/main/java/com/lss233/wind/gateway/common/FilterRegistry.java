package com.lss233.wind.gateway.common;

import com.lss233.wind.gateway.service.http.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author : yjp
 * @Date : 2022/5/14 17:21
 */
public class FilterRegistry {
    private final static Logger LOG = LoggerFactory.getLogger(FilterRegistry.class);

    private final Map<String, Class<? extends Filter>> filterMap = new HashMap<>();
    public void init() {
        filterMap.clear();
        LOG.info("Initializing Filter Registry...");

        filterMap.put("IpAccept", IpAccept.class);
        filterMap.put("CORS", CORS.class);
        filterMap.put("RewriteHeadersFilter", RewriteHeadersFilter.class);
        filterMap.put("FlowLimiter", FlowLimitFilter.class);
        filterMap.put("UriBlocker", UriBlocker.class);
        filterMap.put("IpRestriction", IpRestriction.class);
        filterMap.put("RefererRestriction", RefererRestriction.class);
        filterMap.put("UaRestriction", UaRestriction.class);
    }

    public Class<? extends Filter> getRegistry(String key) {
        return filterMap.get(key);
    }

    public Set<String> getKeys() {
        return filterMap.keySet();
    }

    public void addRegistry(String key, Class<? extends Filter> filter) {
        filterMap.put(key, filter);
    }
}
