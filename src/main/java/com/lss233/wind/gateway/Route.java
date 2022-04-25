package com.lss233.wind.gateway;

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
}
