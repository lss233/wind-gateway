package com.lss233.wind.gateway.service.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.entity.UpstreamItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * Author: icebigpig
 * Data: 2022/4/23 13:21
 * Version 1.0
 **/

public class ConsulApi {
    ConsulClient client = new ConsulClient("localhost",8500);

    public void setKV(){
        // set KV

        // JSON.toJSONString(listOfEntity);实现序列化

        byte[] binaryData = new byte[] {1,2,3,4,5,6,7};
        client.setKVBinaryValue("someKey", binaryData);
        client.setKVValue("com.my.app.foo", "foo");

    }

    public void getSingleKVForKey(){
        // 获取密钥的单个 KV
        Response<GetValue> keyValueResponse = client.getKVValue("com.my.app.foo");
        System.out.println(keyValueResponse.getValue().getKey() + ": " + keyValueResponse.getValue().getDecodedValue());
        // prints "com.my.app.foo: foo"
    }

    public void getPrefixKVsList(){
        // (递归)获取键前缀的 KV 列表
        Response<List<GetValue>> keyValuesResponse = client.getKVValues("com.my");
        keyValuesResponse.getValue().forEach(value -> System.out.println(value.getKey() + ": " + value.getDecodedValue()));
        // prints "com.my.app.foo: foo" and "com.my.app.bar: bar"
    }

    public void getKnownDatacenters(){
        // 列出已知的数据中心
        Response<List<String>> response = client.getCatalogDatacenters();
        System.out.println("Datacenters: " + response.getValue());
    }

    public void RegisterService(){
        // 注册新服务
        NewService newService = new NewService();
        newService.setId("myapp_01");
        newService.setName("myapp");
        newService.setTags(Arrays.asList("EU-West", "EU-East"));
        newService.setPort(8080);

        // 使用相关的健康检查注册新服务
        NewService.Check serviceCheck = new NewService.Check();
        serviceCheck.setScript("/usr/bin/some-check-script");
        serviceCheck.setInterval("10s");
        newService.setCheck(serviceCheck);

        client.agentServiceRegister(newService);
    }

    public void CheckServicesHealthy(){

//        // 根据名称查询健康服务(如果健康则返回 myapp_01 和 myapp_02)
//        HealthServicesRequest request = HealthServicesRequest.newBuilder()
//                .setPassing(true)
//                .setQueryParams(QueryParams.DEFAULT)
//                .build();
//        Response<List<HealthService>> healthyServices = client.getHealthServices("myapp", request);

        // 根据名称和标签查询健康服务(如果健康则返回 myapp_01)
        HealthServicesRequest request = HealthServicesRequest.newBuilder()
                .setTag("EU-West")
                .setPassing(true)
                .setQueryParams(QueryParams.DEFAULT)
                .build();
        Response<List<HealthService>> healthyServices = client.getHealthServices("myapp", request);
        System.out.println(healthyServices);
    }

    public static void main(String[] args) throws JsonProcessingException {
        ConsulApi consulApi = new ConsulApi();

//        consulApi.getPrefixKVsList();

        consulApi.CheckServicesHealthy();

        consulApi.getKnownDatacenters();


        UpstreamItem upstreamItem1 = new UpstreamItem("123",123,123,true);
        UpstreamItem upstreamItem2 = new UpstreamItem("123",13,12,true);
        List<UpstreamItem> upstreamItemList = new ArrayList<>();
        upstreamItemList.add(upstreamItem1);
        upstreamItemList.add(upstreamItem2);

        //序列化
        ObjectMapper mapper = new ObjectMapper();
        String str = mapper.writeValueAsString(upstreamItemList);

        System.out.println(str);

        ObjectMapper mapper1 = new ObjectMapper();

        // json 转数组对象


        UpstreamItem[] person2 = mapper1.readValue(str,UpstreamItem[].class);
        for(UpstreamItem person1:person2)
            System.out.println(person1);

    }

}

