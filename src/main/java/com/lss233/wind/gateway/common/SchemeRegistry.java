package com.lss233.wind.gateway.common;

import com.lss233.wind.gateway.common.data.Registry;
import com.lss233.wind.gateway.service.http.HttpScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class SchemeRegistry extends Registry<Scheme> {
    private final static Logger LOG = LoggerFactory.getLogger(SchemeRegistry.class);

    @Override
    public void init() {

        LOG.info("Initializing Scheme Registry...");

        registries.put("HTTP", HttpScheme.class);
        registries.put("HTTPS", HttpScheme.class);
    }
}
