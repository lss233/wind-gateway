package com.lss233.wind.gateway.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.web.service.UpstreamService;
import com.lss233.wind.gateway.web.service.impl.UpstreamServiceImpl;
import io.javalin.http.Context;


/**
 * @author zzl
 * @date 2022/5/12 11:10
 */
public class UpstreamController {

    public static UpstreamService upstreamService = new UpstreamServiceImpl();

    /**
     * 配置上游，不存在则创建，存在则修改
     * @param context 用户从中获取前端提交的上游信息， 返回数据
     * @return
     */
    public static void setUpstream(Context context) {
        Upstream upstream = context.bodyAsClass(Upstream.class);
        context.json(upstreamService.setUpstream(upstream));
    }

    /**
     * 获取上游信息
     * @param context 用于从中获取指定的上游名，返回数据
     * @return
     */
    public static void getUpstream(Context context) {
        String upstreamName = context.pathParam("upstreamName");
        context.json(upstreamService.getUpstream(upstreamName));
    }

    /**
     * 用于展示上游列表
     * @param context 用于返回数据
     * @return
     */
    public static void getUpstreams(Context context) {
        context.json(upstreamService.getUpstreams());
    }

    /**
     * 删除指定上游
     * @param context 获取上游名，返回数据
     * @return
     */
    public static void deleteUpstream(Context context) {
        String upstreamName = context.formParam("upstreamName");
        context.json(upstreamService.deleteUpstream(upstreamName));
    }

    /**
     * 模糊查询上游
     * @param context
     */
    public static void search(Context context) {
        String upstreamName = context.formParam("upstreamName");
        context.json(upstreamService.search(upstreamName));
    }
}
