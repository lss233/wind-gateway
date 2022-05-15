package com.lss233.wind.gateway.web.controller;

import com.ecwid.consul.v1.agent.model.NewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.web.service.Service;
import com.lss233.wind.gateway.web.service.impl.ServiceImpl;
import io.javalin.http.Context;

/**
 * @author zzl
 * @date 2022/5/12 23:10
 */
public class ServiceController {

    private static Service service = new ServiceImpl();

    public static void registerService(Context context) {
        NewService newService = context.bodyAsClass(NewService.class);
        context.json(service.registerService(newService));
    }

    public static void getService(Context context) throws JsonProcessingException {
        String serviceName = context.pathParam("serviceName");
        System.out.println(serviceName);
        context.json(service.getService(serviceName));
    }

    public static void deregister(Context context) {
        String serviceName = context.formParam("serviceName");
        context.json(service.deleteService(serviceName));
    }
}
