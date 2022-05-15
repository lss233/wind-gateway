package com.lss233.wind.gateway.common.data;

import com.lss233.wind.gateway.common.Filter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class Registry<T>{
    protected final Map<String, Class<? extends T>> registries = new HashMap<>();
    public abstract void init();
    public abstract Class<? extends T> getRegistry(String key);

    public abstract Set<String> getKeys();

    public abstract void addRegistry(String key, Class<? extends T> filter);


}
