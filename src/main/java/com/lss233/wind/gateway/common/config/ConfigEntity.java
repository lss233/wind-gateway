package com.lss233.wind.gateway.common.config;

/**
 * Author: icebigpig
 * Data: 2022/5/13 16:06
 * Version 1.0
 **/

public class ConfigEntity {

    /**
     * Consul 数据中心地址
     */
    private String consulURL = "localhost";

    /**
     * Consul 端口
     */
    private Integer consulPort = 8500;

    /**
     * 服务端口号
     */
    private Integer servicePort = 80;

    /**
     * web配置面板服务端口
     */
    private Integer webServicePort = 7000;

    public ConfigEntity() {
    }

    public ConfigEntity(String consulURL, Integer consulPort, Integer servicePort, Integer webServicePort) {
        this.consulURL = consulURL;
        this.consulPort = consulPort;
        this.servicePort = servicePort;
        this.webServicePort = webServicePort;
    }

    public String getConsulURL() {
        return this.consulURL;
    }

    public void setConsulURL(String consulURL) {
        this.consulURL = consulURL;
    }

    public Integer getConsulPort() {
        return this.consulPort;
    }

    public void setConsulPort(Integer consulPort) {
        this.consulPort = consulPort;
    }

    public Integer getServicePort() {
        return this.servicePort;
    }

    public void setServicePort(Integer servicePort) {
        this.servicePort = servicePort;
    }

    public Integer getWebServicePort() {
        return webServicePort;
    }

    public void setWebServicePort(Integer webServicePort) {
        this.webServicePort = webServicePort;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "consulURL=" + consulURL +
                ", ServicePort='" + servicePort + '\'' +
                ", consulPort='" + consulPort + '\'' +
                ", webServicePort='" + webServicePort + '\'' +
                '}';
    }
}
