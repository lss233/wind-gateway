package com.lss233.wind.gateway.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.web.util.MyResult;

import java.util.List;

/**
 * @author zzl
 * @date 2022/5/12 10:41
 */
public interface UpstreamService {

    /**
     * 配置上游
     * @param upstream
     * @return
     */
    MyResult setUpstream(Upstream upstream);

    /**
     * 获取上游信息
     * @param upstreamName 指定的上游名
     * @return
     */
    MyResult<Upstream> getUpstream(String upstreamName);

    /**
     * 用于展示上游列表
     * @return
     */
    MyResult<List<Upstream>> getUpstreams();

    /**
     * 删除指定上游
     * @param upstreamName 上游名
     * @return
     */
    MyResult deleteUpstream(String upstreamName);

    /**
     * 通过上游名关键词模糊搜索上游
     * @param upstreamName 上游名关键词
     * @return
     */
    MyResult search(String upstreamName);
}
