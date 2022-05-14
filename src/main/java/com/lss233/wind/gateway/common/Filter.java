package com.lss233.wind.gateway.common;

import java.util.Map;

public class Filter {
    private String name;
    private transient Route route;
    private boolean enable;
    private Map<Object, Object> configuration;

    public Filter(String name) {
        this.name = name;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<Object, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<Object, Object> configuration) {
        this.configuration = configuration;
    }
}
