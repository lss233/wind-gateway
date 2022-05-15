package com.lss233.wind.gateway.web.service.impl;

import com.lss233.wind.gateway.common.Filter;
import com.lss233.wind.gateway.service.http.HttpRoute;
import com.lss233.wind.gateway.service.http.filter.*;
import com.lss233.wind.gateway.web.util.FilterConvertor;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;
import org.eclipse.jetty.client.HttpRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author zzl
 * @date 2022/5/13 22:56
 */
public class PluginServiceImpl {
//
//    /**
//     * 将前端传回来的route中的插件父类转为对应的子类。
//     * @param route
//     * @return
//     */
//    public MyResult startPlugin(HttpRoute route) {
//        List<Filter> filterList = route.getFilters();
//        List<Filter> newFilterList = new ArrayList<>();
//        for (Filter filter : filterList) {
//            String pluginName = filter.getName();
//            Filter plugin = FilterConvertor.toPlugin(filter);
//            newFilterList.add(plugin);
//        }
//        route.setFilters(newFilterList);
//        return MyResult.success();
//    }
}
