package com.lss233.wind.gateway.service.consul;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.common.Upstream;
import com.lss233.wind.gateway.service.consul.Utils.ConvertUtils;
import com.lss233.wind.gateway.service.consul.entity.UpstreamConvert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Author: icebigpig
 * Data: 2022/5/1 15:25
 * Version 1.0
 **/

public class UpstreamInfo {

    private static final ConsulApi consulApi = new ConsulApi();

    /**
     * 获取存储在consul中的upstream列表
     * @return List<Upstream>
     */
    public static List<Upstream> getUpstreams() throws JsonProcessingException {

        List<Upstream> upstreamList = new ArrayList<>();
        String valueResponse = consulApi.getSingleKVForKey("upstreamList");
        if (valueResponse == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        // json 转数组对象
        UpstreamConvert[] upstreamConverts = mapper.readValue(valueResponse, UpstreamConvert[].class);
        for(UpstreamConvert upstreamConvert : upstreamConverts){
            upstreamList.add(ConvertUtils.toConvertNormalForm(upstreamConvert));
        }
        return upstreamList;
    }

    /**
     * 将List<Upstream> 序列化并存储到consul中
     */
    public static void setUpstreamList(List<Upstream> upstreamList) throws JsonProcessingException {
        //序列化
        List<UpstreamConvert> upstreamConvertList = new ArrayList<>();
        for(Upstream upstream : upstreamList){
            upstreamConvertList.add(ConvertUtils.toConvertStoreForm(upstream));
        }
        ObjectMapper mapper = new ObjectMapper();
        consulApi.setKVValue("upstreamList",mapper.writeValueAsString(upstreamConvertList));
    }

    public static void updateUpstreamList(List<Upstream> updateUpstreamList) throws JsonProcessingException {
        consulApi.deleteKVValues("upstreamList");
        UpstreamInfo.setUpstreamList(updateUpstreamList);
    }

    /**
     * 通过上游名称获取单个上游服务信息
     */
    public static Upstream getUpstream(String upstreamName) throws JsonProcessingException {

        List<Upstream> upstreamList = UpstreamInfo.getUpstreams();
        if (upstreamList == null) {
            return null;
        }
        for(Upstream upstream : upstreamList){
            if(Objects.equals(upstream.getName(), upstreamName)){
                return upstream;
            }
        }
        return null;
    }

    /**
     * 通过已有上游服务名称修改路由，若不存在该上游服务名称，则进行追加服务
     */
    public static boolean setUpstream(Upstream updateUpstream) throws JsonProcessingException {

        boolean status;
        // 修改前结果集
        List<Upstream> upstreamList = UpstreamInfo.getUpstreams();

        if(upstreamList == null) {
            upstreamList = new ArrayList<>();
        }

        // 待更新的结果集
        List<Upstream> updateUpstreamList = new ArrayList<>();

        // 如果存在该上游服务则获取到
        Upstream upstream = UpstreamInfo.getUpstream(updateUpstream.getName());

        if(upstream == null) {
            status = false;
            // 若不存在，进行追加
            upstreamList.add(updateUpstream);
            UpstreamInfo.updateUpstreamList(upstreamList);
        } else {
            status = true;
            // 若该上游服务信息存在，则进行更新
            for(Upstream upstreamItem : upstreamList){
                if(Objects.equals(upstreamItem.getName(), updateUpstream.getName())){
                    updateUpstreamList.add(updateUpstream);
                } else {
                    updateUpstreamList.add(upstreamItem);
                }
            }

            // TODO 将新的列表数据直接返回更新
            UpstreamInfo.updateUpstreamList(updateUpstreamList);
        }
        return status;
    }


    /**
     * 删除单个上游服务信息
     * 若返回值true则代表存在并且删除成功
     * 若返回false则表示不存在该路由信息
     */
    public static boolean delUpstream(String upstreamName) throws JsonProcessingException {

        // 修改前结果集
        List<Upstream> upstreamList = UpstreamInfo.getUpstreams();
        if (upstreamList == null){
            return false;
        }

        // 修改后待更新的数据集合
        List<Upstream> updateUpstreamList = new ArrayList<>();

        // 获取是用于判断是否已存在该上游信息
        Upstream upstream = getUpstream(upstreamName);
        if (upstream == null) {
            // 若不存在，返回false
            return false;
        } else {
            // 存在该路由信息，则进行更新
            for(Upstream upstreamItem : upstreamList){
                if(Objects.equals(upstreamItem.getName(),upstreamName)){
                    continue;
                } else {
                    updateUpstreamList.add(upstreamItem);
                }
            }

            // 将新的列表数据直接返回更新
            UpstreamInfo.updateUpstreamList(updateUpstreamList);
        }
        return true;
    }

}
