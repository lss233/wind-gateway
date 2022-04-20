package com.lss233.wind.gateway.common;

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
    protected LoadBalancer loadBalancer;


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
    }

}
