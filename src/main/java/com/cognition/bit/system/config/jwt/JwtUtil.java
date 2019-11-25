package com.cognition.bit.system.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.cognition.bit.common.config.ProjectConfig;
import com.cognition.bit.system.config.ApplicationContextRegister;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Jwt token工具，提供JWT生成效验
 * @author Worry
 * @version 2019/9/27
 */
public class JwtUtil {

    /**
     * 过期时间
     */
//    private static final long EXPIRE_TIME_PRESET = 24 * 60 * 1000;
    private static final long EXPIRE_TIME_PRESET =  1000;

    /**
     * 效验token是否正确
     * @param token
     * @param userId
     * @param password
     * @return
     */
    public static boolean verify(String token, String userId, String password) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(password);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("uid", userId).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取登录名
     *
     * @param token 传入值编码获取
     * @return
     */
    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("uid").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名
     *
     *
     * @param password
     * @return
     */
    public static String sign(Long userId, String password) {
        try {
            ProjectConfig projectConfig = ApplicationContextRegister.getBean(ProjectConfig.class);
            long time = EXPIRE_TIME_PRESET * projectConfig.getJwtTime();
            // 指定过期时间
            Date date = new Date(System.currentTimeMillis() + time);

            Algorithm algorithm = Algorithm.HMAC256(password);
            return JWT.create()
                    .withClaim("uid", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


}
