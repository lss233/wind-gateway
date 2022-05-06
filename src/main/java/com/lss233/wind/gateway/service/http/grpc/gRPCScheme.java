package com.lss233.wind.gateway.service.http.grpc;

import com.lss233.wind.gateway.common.Scheme;

import java.util.Collections;
import java.util.List;

/**
 * @Author : yjp
 * @Date : 2022/5/4 21:31
 */
public class gRPCScheme implements Scheme {
    @Override
    public String getName() {
        return "gRPC";
    }

    @Override
    public List<String> getAliases() {
        return Collections.emptyList();
    }
}
