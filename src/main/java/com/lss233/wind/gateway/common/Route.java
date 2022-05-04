package com.lss233.wind.gateway.common;

import java.util.List;

/**
 * 定义一条路由
 */
public class Route {
    /**
     * 名称，唯一，用于标识路由
     */
    protected String name;
    /**
     * 描述
     */
    protected String description;
    /**
     * 是否上线
     */
    protected boolean publish;

    protected Upstream upstream;

    protected List<Filter> filters;

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

    public boolean isPublish() {
        return publish;
    }

    public void setPublish(boolean publish) {
        this.publish = publish;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public Upstream getUpstream() {
        return upstream;
    }

    public void setUpstream(Upstream upstream) {
        this.upstream = upstream;
    }

    public Route() {
    }

}
