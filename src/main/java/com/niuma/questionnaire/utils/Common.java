package com.niuma.questionnaire.utils;

import com.alibaba.fastjson2.JSON;
import com.niuma.questionnaire.entity.User;
import io.jsonwebtoken.Claims;

import javax.servlet.http.HttpServletRequest;

public class Common {
    public static String JwtUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        Claims claims = JwtUtil.parseJWT(token);
        return claims.getSubject();
    }
}
