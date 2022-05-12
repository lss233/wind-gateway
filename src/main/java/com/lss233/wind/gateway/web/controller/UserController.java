package com.lss233.wind.gateway.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lss233.wind.gateway.web.service.UserService;
import com.lss233.wind.gateway.web.service.impl.UserServiceImp;
import io.javalin.http.Context;

/**
 * @author zzl
 * @date 2022/5/5 10:44
 */
public class UserController {

    public static UserService userService = new UserServiceImp();

    /**
     * 登录接口
     * @param context
     * @throws JsonProcessingException
     */
    public static void login(Context context) throws JsonProcessingException {
        context.json(userService.login(context));
    }

    /**
     * 注册，后来得知不需要注册操作，细节就没有完善
     * @param context
     * @throws JsonProcessingException
     */
    public static void register(Context context) throws JsonProcessingException {
        userService.register(context);
    }
}
