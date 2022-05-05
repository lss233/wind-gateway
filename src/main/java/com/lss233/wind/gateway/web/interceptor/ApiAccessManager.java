package com.lss233.wind.gateway.web.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.lss233.wind.gateway.web.entity.AccessURL;
import com.lss233.wind.gateway.web.util.JavalinJWT;
import com.lss233.wind.gateway.web.util.JwtUtils;
import io.javalin.core.security.AccessManager;
import io.javalin.core.security.RouteRole;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.lss233.wind.gateway.web.entity.AccessURL.ADMIN;
import static com.lss233.wind.gateway.web.entity.AccessURL.ANYONE;
import static com.lss233.wind.gateway.web.util.JavalinJWT.CONTEXT_ATTRIBUTE;

/**
 * @author zzl
 * @date 2022/5/4 20:17
 */
public class ApiAccessManager implements AccessManager {

    //查询是否有携带令牌，获取其权限。
    private Set<RouteRole> extractRole(Context context) {
        Set<RouteRole> myUrl = new LinkedHashSet<>();
        if (context.attribute(CONTEXT_ATTRIBUTE) == null) {
            myUrl.add(ANYONE);
            return myUrl;
        }

        String jwt = context.attribute("jwt");
        Map<String, String> dataMap = JwtUtils.verifyJwt(jwt);
        // TODO: 2022/5/5 将其转换为原来的类型
//        myUrl = dataMap.get("myUrls");
//        DecodedJWT jwt = context.attribute("jwt");
//        assert jwt != null;

//        return (Set<RouteRole>) jwt.getClaim("myUrls");
        return null;
    }

    @Override
    public void manage(@NotNull Handler handler, @NotNull Context context, @NotNull Set<RouteRole> permittedRoles) throws Exception {

        Set<RouteRole> myUrl = extractRole(context);
        System.out.println(myUrl);
        //查看是否包含role
        if (myUrl.contains(ANYONE) || Authentication.authenticate(permittedRoles, myUrl)) {
            handler.handle(context);
        } else {
            context.status(401).result("Unauthorized");
        }
    }
}
