package com.lss233.wind.gateway.web.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.javalin.http.Context;
import io.javalin.http.InternalServerErrorResponse;

import java.util.Optional;

public class JavalinJWT {

    public final static String CONTEXT_ATTRIBUTE = "jwt";
    public final static String COOKIE_KEY = "jwt";


    public static boolean containsJWT(Context context) {
        return context.attribute(CONTEXT_ATTRIBUTE) != null;
    }

    //添加解码到context中
    public static Context addDecodedToContext(Context context, DecodedJWT jwt) {
        context.attribute(CONTEXT_ATTRIBUTE, jwt);
        return context;
    }

    //获取context中获取token
    public static DecodedJWT getDecodedFromContext(Context context) {
        Object attribute = context.attribute(CONTEXT_ATTRIBUTE);

        System.out.println("getDecodeFormContext::::::::>"+((DecodedJWT) attribute).getClaim("level"));
        if (!(attribute instanceof DecodedJWT)) {
            throw new InternalServerErrorResponse("The context carried invalid object as JavalinJWT");
        }

        return (DecodedJWT) attribute;
    }

    //从请求头获取token
    public static Optional<String> getTokenFromHeader(Context context) {
        return Optional.ofNullable(context.header("Authorization"))
                .flatMap(header -> {
                    String[] split = header.split(" ");
                    if (split.length != 2 || !split[0].equals("Bearer")) {
                        return Optional.empty();
                    }

                    return Optional.of(split[1]);
                });
    }

    //从cookie中获取token
    public static Optional<String> getTokenFromCookie(Context context) {
        return Optional.ofNullable(context.cookie(COOKIE_KEY));
    }

    public static Context addTokenToCookie(Context context, String token) {
        return context.cookie(COOKIE_KEY, token);
    }

}
