package com.lss233.wind.gateway.common;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


/**
 * 定义一个上游服务
 */
public class Upstream {
    /**
     * 名称，唯一，用于标识服务。
     */
    protected String name;
    /**
     * 描述
     */
    protected String description;
    /**
     * 节点列表
     */
    protected List<Destination> endpoints;
    /**
     * 重试次数，0为不重试
     */
    protected long retryAttempts;
    /**
     * 重试超时时间，毫秒
     */
    protected long retryTimeout;
    /**
     * 连接超时时间，毫秒
     */
    protected long connectTimeout;
    /**
     * 发送超时时间，毫秒
     */
    protected long sendTimeout;
    /**
     * 接收超时时间，毫秒
     */
    protected long receiveTimeout;
    /**
     * 协议
     */
    protected Scheme scheme;
    /**
     * 负载均衡算法
     */
    protected Class<? extends LoadBalancer> loadBalancerClass;

    private transient LoadBalancer loadBalancer;


    public static class Destination {
        /**
         * 主机
         */
        protected String host;
        /**
         * 端口号
         */
        protected int port;
        /**
         * 权重
         */
        protected int weight;

        /**
         * 是否在线
         */
        protected boolean online;

        public Destination(String host, int port, int weight, boolean online) {
            this.host = host;
            this.port = port;
            this.weight = weight;
            this.online = online;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public boolean isOnline() {
            return online;
        }

        public void setOnline(boolean online) {
            this.online = online;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Destination> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(List<Destination> endpoints) {
        this.endpoints = endpoints;
    }

    public long getRetryAttempts() {
        return retryAttempts;
    }

    public void setRetryAttempts(long retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public long getRetryTimeout() {
        return retryTimeout;
    }

    public void setRetryTimeout(long retryTimeout) {
        this.retryTimeout = retryTimeout;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public long getSendTimeout() {
        return sendTimeout;
    }

    public void setSendTimeout(long sendTimeout) {
        this.sendTimeout = sendTimeout;
    }

    public long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public Scheme getScheme() {
        return scheme;
    }

    public void setScheme(Scheme scheme) {
        this.scheme = scheme;
    }

    public Class<? extends LoadBalancer> getLoadBalancerClass() {
        return loadBalancerClass;
    }

    public void setLoadBalancerClass(Class<? extends LoadBalancer> loadBalancerClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.loadBalancerClass = loadBalancerClass;
        Constructor<? extends LoadBalancer> constructor = this.loadBalancerClass.getConstructor(List.class);
        constructor.setAccessible(true);
        this.loadBalancer = constructor.newInstance(getEndpoints());
    }

    private LoadBalancer getLoadBalancer() {
        return loadBalancer;
    }

    public Destination chooseDestination() {
        return getLoadBalancer().getLoadBalance();
    }

    public Upstream() {
    }
}
