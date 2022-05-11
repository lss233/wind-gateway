package com.lss233.wind.gateway.service.consul.Utils;

import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.entity.UpstreamConvert;

/**
 * Author: icebigpig
 * Data: 2022/5/11 21:23
 * Version 1.0
 **/

public class ConvertUtils {

    public static UpstreamConvert toConvertStoreForm(Upstream upstream){

        UpstreamConvert upstreamConvert = new UpstreamConvert();

        upstreamConvert.setName(upstream.getName());
        upstreamConvert.setDescription(upstream.getDescription());
        upstreamConvert.setEndpoints( upstream.getEndpoints());
        upstreamConvert.setRetryAttempts(upstream.getRetryAttempts());
        upstreamConvert.setRetryTimeout(upstream.getRetryTimeout());
        upstreamConvert.setConnectTimeout(upstream.getConnectTimeout());
        upstreamConvert.setSendTimeout(upstream.getSendTimeout());
        upstreamConvert.setReceiveTimeout(upstream.getReceiveTimeout());
        upstreamConvert.setScheme(upstream.getScheme());

        return upstreamConvert;
    }

    public static Upstream toConvertNormalForm(UpstreamConvert upstreamConvert){

        Upstream upstream = new Upstream();

        upstream.setName(upstreamConvert.getName());
        upstream.setDescription(upstreamConvert.getDescription());
        upstream.setEndpoints( upstreamConvert.getEndpoints());
        upstream.setRetryAttempts(upstreamConvert.getRetryAttempts());
        upstream.setRetryTimeout(upstreamConvert.getRetryTimeout());
        upstream.setConnectTimeout(upstreamConvert.getConnectTimeout());
        upstream.setSendTimeout(upstreamConvert.getSendTimeout());
        upstream.setReceiveTimeout(upstreamConvert.getReceiveTimeout());
        upstream.setScheme(upstreamConvert.getScheme());

        return upstream;
    }

}
