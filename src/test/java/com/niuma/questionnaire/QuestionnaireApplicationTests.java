package com.niuma.questionnaire;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.questionnaire.dao.UserMapper;
import com.niuma.questionnaire.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootTest
class QuestionnaireApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder encoder;

    @Test
    void contextLoads() {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.select(User::getId).eq(User::getId,1);
        List<User> user = userMapper.selectList(qw);
        System.out.println(user);
    }

    @Test
    void test(){
        System.out.println(encoder.encode("123456"));
    }

}
