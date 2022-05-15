package com.lss233.wind.gateway.service.http.filter;

import com.lss233.wind.gateway.common.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author : yjp
 * @Date : 2022/5/14 17:21
 */
public class FilterRegistry {
    private final static Logger LOG = LoggerFactory.getLogger(FilterRegistry.class);

    private final Map<String, Class<? extends Filter>> filterMap = new HashMap<>();
    public void init() {
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
}
