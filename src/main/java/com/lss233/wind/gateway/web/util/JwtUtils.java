package com.lss233.wind.gateway.web.util;

/**
 * @author zzl
 * @date 2022/5/4 9:47
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import kotlin.collections.MapsKt;

import java.io.UnsupportedEncodingException;
import java.util.*;

// 使用io.jsonwebtoken包
public class JwtUtils {


    private static final String JWT_SECRET_KEY = "^%*&sda89*)_+231AZl$!@FjuT";
    private static final String JWT_ISSUER = "fjut-GateWay-f5";

    public static String generateJwt(Map<String, String> claims) {
        try{
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);  //生成token的算法
            JWTCreator.Builder builder = JWT.create().withIssuer(JWT_ISSUER).withExpiresAt(getExpirationDate()); //设置过期时间为3天
            claims.forEach(builder::withClaim);  //将claims存储到token中
            return builder.sign(algorithm).toString();  //签名并且返回token
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date getExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        return calendar.getTime();
    }

    public static Map<String, String> verifyJwt(String token) {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JWT_ISSUER)
                    .build();
            DecodedJWT decoded = verifier.verify(token);
            //从token取出数据
            Map<String, Claim> map = decoded.getClaims();
            Map<String, String> dataMap = new HashMap<>();
            //赋值入dataMap中
            map.forEach((k,v) -> dataMap.put(k, v.asString()));
            return dataMap;
    }

}
