package com.lss233.wind.gateway.common;

import com.lss233.wind.gateway.common.data.Registry;
import com.lss233.wind.gateway.service.http.HttpScheme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

public class SchemeRegistry extends Registry<Scheme> {
    private final static Logger LOG = LoggerFactory.getLogger(SchemeRegistry.class);
    private static SchemeRegistry instance;

    public static SchemeRegistry getInstance() {
        if(instance == null) {
            instance = new SchemeRegistry();
            instance.init();
        }
        return instance;
    }

    @Override
    public void init() {

        LOG.info("Initializing Scheme Registry...");

        registries.put("HTTP", HttpScheme.class);
        registries.put("HTTPS", HttpScheme.class);
    }

    public String getKey(Class<? extends Scheme> value) {
        for (Map.Entry<String, Class<? extends Scheme>> entry : registries.entrySet()) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
