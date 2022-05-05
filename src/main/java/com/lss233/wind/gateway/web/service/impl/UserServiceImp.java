package com.lss233.wind.gateway.web.service.impl;

import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.service.consul.ConsulApi;
import com.lss233.wind.gateway.web.dao.UserConsulDao;
import com.lss233.wind.gateway.web.entity.User;
import com.lss233.wind.gateway.web.service.UserService;
import com.lss233.wind.gateway.web.util.JwtUtils;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zzl
 * @date 2022/5/5 19:43
 */
public class UserServiceImp implements UserService {

    public static UserConsulDao userConsulDao = new UserConsulDao();

    @Override
    public void login(Context context) throws JsonProcessingException {
        //获取前端传过来的账号密码
        String username = context.queryParam("username");
        String password = context.queryParam("password");
        System.out.println(username + "       "+ password);
        User user = userConsulDao.getUserFromConsulByKey(username);
        assert user!=null;
        if (user.getPassword().equals(password)) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", username);
            claims.put("myUrls", user.getMyUrls().toString());
            String jwt = JwtUtils.generateJwt(claims);
            //将jwt写入cookie
            context.cookie("jwt", jwt);
            context.result("登录成功");
        } else {
            context.result("账号或密码错误!");
        }
    }

    @Override
    public void register(Context context) throws JsonProcessingException {
        String username = context.attribute("username");
        String password = context.attribute("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userConsulDao.storeUserToConsul(user);
        context.result("注册成功");
    }
}
