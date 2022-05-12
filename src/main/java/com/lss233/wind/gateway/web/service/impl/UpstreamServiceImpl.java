package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.UpstreamInfo;
import com.lss233.wind.gateway.web.service.UpstreamService;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;
import java.util.List;

/**
 * @author zzl
 * @date 2022/5/12 10:42
 */
public class UpstreamServiceImpl implements UpstreamService {

    @Override
    public MyResult setUpstream(Upstream upstream){
        if (upstream == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "路由信息不能全为null", null);
        }
        try {
            UpstreamInfo.setUpstream(upstream);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR.getCode(), "配置上游失败", upstream);
        }
        return MyResult.success(upstream);
    }

    @Override
    public MyResult<Upstream> getUpstream(String upstreamName) {
        if (upstreamName == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "上游名不可为null", null);
        }
        Upstream upstream;
        try {
            upstream = UpstreamInfo.getUpstream(upstreamName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (upstream == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(),"没有找到名为"+upstreamName+"的上游", null);
        }
        return MyResult.success(upstream);
    }

    @Override
    public MyResult<List<Upstream>> getUpstreams() {
        List<Upstream> upstreamList;
        try {
            upstreamList = UpstreamInfo.getUpstreams();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (upstreamList == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND.getCode(), "不存在任何的上游，请先创建", null);
        }
        return MyResult.success(upstreamList);
    }

    @Override
    public MyResult deleteUpstream(String upstreamName) {
        if (upstreamName == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "需要删除的上游的上游名不可为空", null);
        }
        boolean b = false;
        try {
            b = UpstreamInfo.delUpstream(upstreamName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (!b) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "删除名为"+upstreamName+"的上游失败", null);
        }
        return MyResult.success();
    }

    @Override
    public MyResult search(String upstreamName) {
        List<Upstream> upstreamList;
        try {
           upstreamList = UpstreamInfo.search(upstreamName);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return MyResult.fail(ResultEnum.ERROR);
        }
        if (upstreamList.isEmpty()) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        return MyResult.success(upstreamList);
    }
}
