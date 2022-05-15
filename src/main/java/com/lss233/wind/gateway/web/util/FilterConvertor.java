package com.lss233.wind.gateway.web.util;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.common.FilterRegistry;
import com.lss233.wind.gateway.service.http.HttpRoute;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zzl
 * @date 2022/5/14 17:42
 */
public class FilterConvertor {

    public static void setPlugin(HttpRoute route) {
        List<Filter> filterList = route.getFilters();
        if (filterList == null) {
            return;
        }
        List<Filter> newFilterList = new ArrayList<>();
        for (Filter filter : filterList) {
            String pluginName = filter.getName();
            Filter plugin = FilterConvertor.toPlugin(filter);
            newFilterList.add(plugin);
        }
        route.setFilters(newFilterList);
    }

    /**
     * 路由中的filter对应具体插件的转换类
     * @param filter
     * @return
     */
    public static Filter toPlugin(Filter filter) {
        String pluginName = filter.getName();
        FilterRegistry filterRegistry = new FilterRegistry();
        filterRegistry.getRegistry(filter.getName()).newInstance();
        return filter;
    }

    /**
     * 插件父类转换具体插件
     * @param filter 插件父类
     * @param plugin 需要转转换成的插件
     * @return
     */
    public static Filter toPlugin(Filter filter, Filter plugin) {
        plugin.setName(filter.getName());
        plugin.setConfiguration(filter.getConfiguration());
        plugin.setEnable(filter.isEnable());
        return plugin;
    }
}
