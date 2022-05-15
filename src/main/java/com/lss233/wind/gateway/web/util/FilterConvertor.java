package com.lss233.wind.gateway.web.util;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.service.http.filter.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author zzl
 * @date 2022/5/14 17:42
 */
public class FilterConvertor {

    public static void setPlugin(HttpRoute route) {
        List<Filter> filterList = route.getFilters();
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
        switch (pluginName) {
            case "CORS": filter = toPlugin(filter, new CORS(pluginName)); break;
            case "FlowLimitFilter": filter = toPlugin(filter, new FlowLimitFilter(pluginName)) ; break;
            case "IpAccept":filter = toPlugin(filter, new IpAccept(pluginName)); break;
            case "IpRestriction":filter = toPlugin(filter, new IpRestriction(pluginName)); break;
            case "RefererRestriction":filter = toPlugin(filter, new RefererRestriction(pluginName)); break;
            case "RewriteHeadersFilter":filter = toPlugin(filter, new RewriteHeadersFilter(pluginName)); break;
            case "UaRestriction":filter = toPlugin(filter, new UaRestriction(pluginName)); break;
            case "UriBlocker":filter = toPlugin(filter, new UriBlocker(pluginName)); break;
            default:return filter;
        }
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
