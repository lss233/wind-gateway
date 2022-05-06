package com.lss233.wind.gateway.web.dao;

import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.web.entity.User;

/**
 * @author zzl
 * @date 2022/5/5 20:07
 */
public class UserConsulDao {

    public String storeUserToConsul(User user) throws JsonProcessingException {
        System.out.println(user.getUsername());
        ConsulApi api = new ConsulApi();

        //序列化
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(user);
        //将keyValue存入consul中
        api.setKVValue(user.getUsername(),userJson);
        return userJson;
    }

    public User getUserFromConsulByKey(String username) throws JsonProcessingException {
        ConsulApi api = new ConsulApi();
        String userJson = api.getSingleKVForKey(username);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userJson, User.class);
        return user;
    }
}
