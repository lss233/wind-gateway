package com.lss233.wind.gateway.web.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.http.Context;

/**
 * @author zzl
 * @date 2022/5/5 19:41
 */
public interface UserService {

    /**
     * 登录
     * @param context 前端传回来的数据
     */
    void login(Context context) throws JsonProcessingException;

    /**
     * 注册
     * @param context 前端传回来的数据
     */
    void register(Context context) throws JsonProcessingException;
}
