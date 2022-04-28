package com.lss233.wind.gateway.service.http;

import com.lss233.wind.gateway.common.Scheme;

import java.util.Collections;
import java.util.List;

public class HttpsScheme implements Scheme {
    @Override
    public String getName() {
        return "HTTPS";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }
}
