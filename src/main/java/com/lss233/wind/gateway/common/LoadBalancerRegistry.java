package com.lss233.wind.gateway.common;

import com.lss233.wind.gateway.common.data.Registry;
import com.lss233.wind.gateway.common.lb.RandomLoadBalancer;

import java.util.Map;
import java.util.Set;

/**
 * @author zzl
 * @date 2022/5/15 18:53
 */
public class LoadBalancerRegistry extends Registry<LoadBalancer> {
    private static LoadBalancerRegistry instance;

    public static LoadBalancerRegistry getInstance() {
        if(instance == null) {
            instance = new LoadBalancerRegistry();
            instance.init();
        }
        return instance;
    }

    @Override
    public void init() {
        registries.clear();;
        registries.put("Random", RandomLoadBalancer.class);
    }

    public String getKey(Class<? extends LoadBalancer> value) {
        for (Map.Entry<String, Class<? extends LoadBalancer>> entry : registries.entrySet()) {
            if(entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
