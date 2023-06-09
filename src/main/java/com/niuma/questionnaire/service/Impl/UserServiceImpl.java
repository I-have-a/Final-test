package com.niuma.questionnaire.service.Impl;

import com.niuma.questionnaire.dao.UserMapper;
import com.niuma.questionnaire.entity.LoginUser;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.UserService;
import com.niuma.questionnaire.utils.CacheClient;
import com.niuma.questionnaire.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    CacheClient cacheClient;
    @Autowired
    UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Integer signup(User user) {
        return userMapper.insert(user);
    }

    @Override
    public String login(HashMap<String, Object> map) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(map.get("account"), map.get("password"));
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userid = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userid);
        //把完整的用户信息存入redis  userid作为key
        cacheClient.set("login:" + userid, loginUser);
        return jwt;
    }

}
