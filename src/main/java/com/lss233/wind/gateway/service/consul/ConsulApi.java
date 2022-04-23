package com.lss233.wind.gateway.service.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;

import java.util.Arrays;
import java.util.List;

/**
 * Author: icebigpig
 * Data: 2022/4/23 13:21
 * Version 1.0
 **/

public class ConsulApi {
    ConsulClient client = new ConsulClient("localhost");

    public void setKV(){
        // set KV
        byte[] binaryData = new byte[] {1,2,3,4,5,6,7};
        client.setKVBinaryValue("someKey", binaryData);
        client.setKVValue("com.my.app.foo", "foo");
        client.setKVValue("com.my.app.bar", "bar");
        client.setKVValue("com.your.app.foo", "hello");
        client.setKVValue("com.your.app.bar", "world");
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
    }
}
