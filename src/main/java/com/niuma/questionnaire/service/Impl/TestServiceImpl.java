package com.niuma.questionnaire.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.questionnaire.dao.TestMapper;
import com.niuma.questionnaire.entity.Test;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    TestMapper testMapper;

    @Override
    public List<Test> getTest(String userID) {
        QueryWrapper<Test> wrapper = new QueryWrapper<>();
        wrapper.eq("UID", userID);
        return testMapper.selectList(wrapper);
    }
}
