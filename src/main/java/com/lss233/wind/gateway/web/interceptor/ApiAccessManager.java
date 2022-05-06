package com.lss233.wind.gateway.web.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lss233.wind.gateway.web.util.JwtUtils;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.lss233.wind.gateway.web.entity.AccessURL.ADMIN;
import static com.lss233.wind.gateway.web.entity.AccessURL.ANYONE;
import static com.lss233.wind.gateway.web.util.JavalinJWT.CONTEXT_ATTRIBUTE;

/**
 * @author zzl
 * @date 2022/5/4 20:17
 */
public class ApiAccessManager implements AccessManager {

    //查询是否有携带令牌，获取其权限。
    private Set extractRole(Context context) throws JsonProcessingException {
        Set myUrl = new HashSet<>();
        if (context.cookie(CONTEXT_ATTRIBUTE) == null) {
            myUrl.add(ANYONE);
            return myUrl;
        }

//        String jwt = context.attribute("jwt");
//        Map<String, String> dataMap = JwtUtils.verifyJwt(jwt);
        // TODO: 2022/5/5 将其转换为原来的类型
//        myUrl = dataMap.get("myUrls");
        String jwt = context.cookie("jwt");
        Map<String, String> myUrls = JwtUtils.verifyJwt(jwt);
        assert jwt != null;
        ObjectMapper objectMapper = new ObjectMapper();
        myUrl =  objectMapper.readValue(myUrls.get("myUrls"), Set.class);
        System.out.println("jwt.getClaim(myUrl):"+myUrl);
        return myUrl;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> permittedRoles) throws Exception {
        Set myUrls = extractRole(context);
        //查看是否包含role
        if (myUrls.contains(ADMIN) || Authentication.authenticate(permittedRoles, myUrls)) {
            handler.handle(context);
        } else {
            context.status(401).result("Unauthorized");
        }
    }
}
