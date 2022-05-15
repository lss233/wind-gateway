package com.lss233.wind.gateway.web.service.impl;


import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.model.HealthService;
import com.google.gson.Gson;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.web.service.Service;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 * @date 2022/5/12 21:17
 */
public class ServiceImpl implements Service {

    static ConsulApi consulApi = new ConsulApi();

    @Override
    public MyResult registerService(NewService newService) {
        try{
            consulApi.RegisterService(newService);
        }catch (Exception e) {
            return MyResult.fail(ResultEnum.ERROR);
        }
        return MyResult.success();
    }

    @Override
    public MyResult updateService(NewService newService) {
        return registerService(newService);
    }

    @Override
    public MyResult getService(String serviceName) {
        if (serviceName == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "serviceName不可为null", null);
        }
        Response<List<HealthService>> listResponse = consulApi.CheckServicesHealthy(serviceName);
        System.out.println(listResponse);
        List<HealthService> healthServices = listResponse.getValue();
        Gson gson = new Gson();
        HealthService.Service service = healthServices.get(0).getService();
        return MyResult.success(service);
    }

    @Override
    public MyResult getServices() {
        Map<String, com.ecwid.consul.v1.agent.model.Service> serviceList = consulApi.getServiceList();
        List<com.ecwid.consul.v1.agent.model.Service> services = new ArrayList<>();
        if (serviceList == null) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        serviceList.forEach((k,v) -> {
            services.add(v);
        });
        return MyResult.success(services);
    }

    @Override
    public MyResult deleteService(String serviceName) {
        try{
            consulApi.deregisterService(serviceName);
        }catch (Exception e){
            return MyResult.fail(ResultEnum.ERROR.getCode(), e.getMessage(), null);
        }
        return MyResult.success("服务注销成功",null);
    }

    @Override
    public MyResult search(String serviceName) {
        if (serviceName == null) {
            serviceName = "";
        }
        List<com.ecwid.consul.v1.agent.model.Service> services = (List<com.ecwid.consul.v1.agent.model.Service>) getServices().getData();
        if (services.isEmpty()) {
            return MyResult.fail(ResultEnum.NOT_FOUND);
        }
        List<com.ecwid.consul.v1.agent.model.Service> serviceList = new ArrayList<>();
        for (com.ecwid.consul.v1.agent.model.Service service : services) {
            if (service.getService().contains(serviceName)) {
                serviceList.add(service);
            }
        }
        return MyResult.success(serviceList);
    }
}
