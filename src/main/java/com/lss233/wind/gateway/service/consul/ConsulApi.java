package com.lss233.wind.gateway.service.consul;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.QueryParams;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.model.NewService;
import com.ecwid.consul.v1.health.HealthServicesRequest;
import com.ecwid.consul.v1.health.model.HealthService;
import com.ecwid.consul.v1.kv.model.GetValue;

import java.util.List;


/**
 * Author: icebigpig
 * Data: 2022/4/23 13:21
 * Version 1.0
 **/

public class ConsulApi {

    ConsulClient client = new ConsulClient("127.0.0.1",8500);
    // TODO 这里需要将配置信息从本地文件读取，在本地文件里配置Yaml文件进行加载配置

    /**
     * 添加KV数值
     * @return Response<Boolean>
     */
    public Response<Boolean> setKVValue(String key,String value){
        // set KV
        return client.setKVValue(key, value);
    }

    /**
     * 获取密钥的单个 KV (根据key获取value)
     * @param  Key
     * @return Response<GetValue>
     */
    public String getSingleKVForKey(String Key){
        // 获取密钥的单个 KV
        Response<GetValue> keyValueResponse = client.getKVValue(Key);
        // prints "com.my.app.foo: foo"
        String value;
        try{
            value = keyValueResponse.getValue().getDecodedValue();
            // TODO: 2022/5/10   这里测试格式使用，上线后注释掉
            // System.out.println(keyValueResponse.getValue().getKey() + ": " + keyValueResponse.getValue().getDecodedValue());
        }catch (NullPointerException e){
            return null;
        }
        return value;
    }

    /**
     * 删除 key 对应的数值
     * @param key
     * @return Response<Void>
     */
    public Response<Void> deleteKVValues(String key) {
        return client.deleteKVValues(key);
    }

    /**
     * (递归)获取键前缀的 KV 列表 (根据key获取value,类似搜索功能，返回前缀所有符合条件的列表)
     * @param keyPrefix
     * @return Response<List<GetValue>>
     */
    public Response<List<GetValue>> getPrefixKVsList(String keyPrefix){
        // (递归)获取键前缀的 KV 列表
        Response<List<GetValue>> keyValuesResponse = client.getKVValues(keyPrefix);
        // TODO 这里测试格式使用，上线后注释掉
        // keyValuesResponse.getValue().forEach(value -> System.out.println(value.getKey() + ": " + value.getDecodedValue()));
        // prints "com.my.app.foo: foo" and "com.my.app.bar: bar"
        return keyValuesResponse;
    }


    /**
     * 列出已知的数据中心
     * @return Response<List<String>>
     */
    public Response<List<String>> getKnownDatacenters(){
        // 列出已知的数据中心
        Response<List<String>> response = client.getCatalogDatacenters();
        // TODO 这里测试格式使用，上线后注释掉
        // System.out.println("Datacenters: " + response.getValue());
        return response;
    }

    /**
     * 注册新服务
     */
    public void RegisterService(NewService newService){
        // 注册新服务
        /*
            使用样例
            NewService newServiceDemo = new NewService();
            newServiceDemo.setId("myapp_01");
            newServiceDemo.setName("myapp");
            newServiceDemo.setTags(Arrays.asList("EU-West", "EU-East"));
            newServiceDemo.setPort(8080);
         */
        /*
            使用相关的健康检查注册新服务
            NewService.Check serviceCheck = new NewService.Check();
            serviceCheck.setScript("/usr/bin/some-check-script");
            serviceCheck.setInterval("10s");
            newService.setCheck(serviceCheck);
        */
        client.agentServiceRegister(newService);
    }

    /**
     * 根据名称查询健康服务
     * @param serviceName
     * @return Response<List<HealthService>>
     */
    public Response<List<HealthService>> CheckServicesHealthy(String serviceName){

        // 根据名称查询健康服务(如果健康则返回 myapp_01 和 myapp_02)
        HealthServicesRequest request = HealthServicesRequest.newBuilder()
                .setPassing(true)
                .setQueryParams(QueryParams.DEFAULT)
                .build();
        Response<List<HealthService>> healthyServices = client.getHealthServices(serviceName, request);
        // TODO 这里测试格式使用，上线后注释掉
        // healthyServices.getValue().forEach(value -> System.out.println(value.toString()));
        return healthyServices;
        /*
            根据名称和标签查询健康服务(如果健康则返回 myapp_01)
            HealthServicesRequest request = HealthServicesRequest.newBuilder()
                    .setTag("EU-West")
                    .setPassing(true)
                    .setQueryParams(QueryParams.DEFAULT)
                    .build();
            Response<List<HealthService>> healthyServices = client.getHealthServices("service-provider-7070", request);
            System.out.println(healthyServices);
         */
    }

    /**
     * 获取节点状态信息
     * @return Response<List<String>>
     */
    public Response<List<String>> getStatusPeers(){
        return client.getStatusPeers();
    }


}

