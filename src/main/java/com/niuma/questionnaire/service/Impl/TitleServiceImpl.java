package com.niuma.questionnaire.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.questionnaire.dao.TitleMapper;
import com.niuma.questionnaire.entity.Title;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TitleServiceImpl implements TitleService {

    @Autowired
    TitleMapper titleMapper;

    @Override
    public List<Title> getUserTitle(String userID) {
        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("UID", userID);
        return titleMapper.selectList(wrapper);
    }

    @Override
    public List<Title> getTestTitle(HashMap<String,Object> map) {
        return titleMapper.getTitleByTestID(map);
    }
}
