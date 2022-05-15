package com.lss233.wind.gateway.common;

import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.StdConverter;

public class SchemeSerializer extends StdConverter<Class<? extends Scheme>, String> {
    @Override
    public String convert(Class<? extends Scheme> value) {
        return new SchemeRegistry().getKey(value);
    }
}
