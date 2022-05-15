package com.lss233.wind.gateway.common;

import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

/**
 * @author zzl
 * @date 2022/5/15 20:11
 */
public class SchemeDeserializer extends StdConverter<String, Class<? extends Scheme>> {

    @Override
    public Class<? extends Scheme> convert(String value) {
        return new SchemeRegistry().getRegistry(value);
    }
}
