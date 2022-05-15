package com.lss233.wind.gateway.web.service;

import com.ecwid.consul.v1.agent.model.NewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.web.util.MyResult;

/**
 * @author zzl
 * @date 2022/5/12 21:17
 */
public interface Service {

    /**
     * 注册服务
     * @param newService
     * @return
     */
    MyResult registerService(NewService newService);

    /**
     * 更新服务
     * @param newService
     * @return
     */
    MyResult updateService(NewService newService);

    /**
     * 服务名获取服务
     * @param serviceName
     * @return
     */
    MyResult getService(String serviceName) throws JsonProcessingException;

    /**
     * 查询服务列表
     * @return
     */
    MyResult getServices();

    /**
     * 通过服务名删除服务
     * @param serviceName
     * @return
     */
    MyResult deleteService(String serviceName);

    /**
     * 模糊搜索服务
     * @param serviceName
     * @return
     */
    MyResult search(String serviceName);
}
