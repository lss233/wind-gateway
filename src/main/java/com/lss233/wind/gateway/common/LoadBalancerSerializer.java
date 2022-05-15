package com.lss233.wind.gateway.common;

import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @author zzl
 * @date 2022/5/15 19:28
 */
public class LoadBalancerSerializer extends StdConverter<Class<? extends LoadBalancer>, String> {
    @Override
    public String convert(Class<? extends LoadBalancer> value) {
        return LoadBalancerRegistry.getInstance().getKey(value);
    }
}
