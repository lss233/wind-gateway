package com.lss233.wind.gateway.service.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.agent.model.Service;
import com.ecwid.consul.v1.health.model.HealthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.entity.UpstreamConvert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Author: icebigpig
 * Data: 2022/5/1 15:25
 * Version 1.0
 **/
public class test {
    public static void main(String[] args) throws JsonProcessingException {

        Upstream upstream = new Upstream();

        upstream.setDescription("test");

        List<Upstream> upstreams = new ArrayList<>();

        upstreams.add(upstream);
        upstreams.add(upstream);


        UpstreamInfo.setUpstreamList(upstreams);

        System.out.println(UpstreamInfo.getUpstream().get(1).getDescription());

        System.out.println("===========================================================================");

        ConsulApi consulApi = new ConsulApi();

        Response<List<HealthService>> response = consulApi.CheckServicesHealthy("serviceProvider");
        //获取服务名称为serviceProvider的上游服务状态
//
//        System.out.println(consulApi.setKVValue(new KeyValue("asd","123")));
//        System.out.println(consulApi.setKVValue(new KeyValue("asd1","123")));
//        System.out.println(consulApi.setKVValue(new KeyValue("as12d","123")));
//        System.out.println("获取已知数据中心" + consulApi.getKnownDatacenters());
//        System.out.println("获取节点信息（非上游服务）" + consulApi.getStatusPeers());
//        System.out.println(consulApi.CheckServicesHealthy("service-provider"));
//        System.out.println(consulApi.getPrefixKVsList(""));


        NewService newServiceDemo = new NewService();
        newServiceDemo.setId("myapp_02");
        newServiceDemo.setName("myapp");
        newServiceDemo.setTags(Arrays.asList("EU-West", "EU-East"));
        newServiceDemo.setPort(8081);

        consulApi.RegisterService(newServiceDemo);

        System.out.println(consulApi.CheckServicesHealthy("service-provider"));

        System.out.println("===========================================================================");

        ConsulClient client = new ConsulClient("127.0.0.1",8500);
        Response<Map<String, Service>> res = client.getAgentServices();
        //获取服务列表
        List<UpstreamConvert> upstreamConvertList1 = new ArrayList<>();

        for(Map.Entry<String, Service> entry:res.getValue().entrySet()){
            System.out.println(entry.getKey()+"--->"+entry.getValue());
        }

        System.out.println(client.getAgentChecks());
        //健康度检查

    }
}
