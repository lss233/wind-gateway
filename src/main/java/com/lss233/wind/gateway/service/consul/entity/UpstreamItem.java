package com.lss233.wind.gateway.service.consul.entity;

/**
 * Author: icebigpig
 * Data: 2022/4/28 23:33
 * Version 1.0
 **/

public class UpstreamItem {

    String host;

    Integer port;

    Integer weight;

    boolean online;


    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public UpstreamItem() {
    }

    public UpstreamItem(String host, Integer port, Integer weight, boolean online) {
        this.host = host;
        this.port = port;
        this.weight = weight;
        this.online = online;
    }

    @Override
    public String toString(){
        return "Upstream{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", weight=" + weight + '\'' +
                ", online='" + online +
                '}';
    }
}
