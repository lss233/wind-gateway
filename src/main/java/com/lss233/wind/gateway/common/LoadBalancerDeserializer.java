package com.lss233.wind.gateway.common;

import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @author zzl
 * @date 2022/5/15 18:26
 */
public class LoadBalancerDeserializer extends StdConverter<String, Class<? extends LoadBalancer>> {
    @Override
    public Class<? extends LoadBalancer> convert(String value) {
        try {
            return LoadBalancerRegistry.getInstance().getRegistry(value);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
