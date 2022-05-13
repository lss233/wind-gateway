package com.lss233.wind.gateway.web.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.web.dao.UserConsulDao;
import com.lss233.wind.gateway.web.entity.User;
import com.lss233.wind.gateway.web.service.UserService;
import com.lss233.wind.gateway.web.util.JwtUtils;
import com.lss233.wind.gateway.web.util.MyResult;
import com.lss233.wind.gateway.web.util.ResultEnum;
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
    public MyResult login(Context context) throws JsonProcessingException {
        //获取前端传过来的账号密码
        String username = context.formParam("username");
        String password = context.formParam("password");
        System.out.println(context.formParamMap());
        System.out.println("#####"+username + "#####" + password);
        User user = userConsulDao.getUserFromConsulByKey(username);
        if (user == null) {
            return MyResult.fail(ResultEnum.ERROR.getCode(), "不存在该账号，请先创建。", null);
        }
        if (user.getPassword().equals(password)) {
            Map<String, String> claims = new HashMap<>();
            claims.put("username", username);
            ObjectMapper jsonMapper = new ObjectMapper();
            String myUrls = jsonMapper.writeValueAsString(user.getMyUrls());
            System.out.println(myUrls);
            claims.put("myUrls", myUrls);
            String jwt = JwtUtils.generateJwt(claims);
            //将jwt写入cookie
            System.out.println(jwt);
            context.cookie("jwt", jwt);
            System.out.println(context.cookie("jwt"));
            return MyResult.success("登录成功");
        } else {
            return MyResult.fail(ResultEnum.NOT_LOGIN,"账号或密码错误");
        }
    }

    @Override
    public void register(Context context) throws JsonProcessingException {
        String username = context.formParam("username");
        String password = context.formParam("password");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        System.out.println(user.toString());
        userConsulDao.storeUserToConsul(user);
        context.result("注册成功");
    }
}
