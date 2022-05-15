package com.lss233.wind.gateway.common.data;

import com.lss233.wind.gateway.common.Filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Registry<T>{
    protected final Map<String, Class<? extends T>> registries = new HashMap<>();
    public abstract void init();
    public Class<? extends T> getRegistry(String key) {
        return registries.get(key);
    }

    public Set<String> getKeys() {
        return registries.keySet();
    }

    public void addRegistry(String key, Class<? extends T> filter) {
        registries.put(key, filter);
    }


}
