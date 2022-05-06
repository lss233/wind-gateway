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

    public static void login(Context context) throws JsonProcessingException {
        userService.login(context);
    }

    public static void register(Context context) throws JsonProcessingException {
        userService.register(context);
    }
}
