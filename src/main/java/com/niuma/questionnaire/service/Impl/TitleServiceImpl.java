package com.niuma.questionnaire.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.questionnaire.dao.TitleMapper;
import com.niuma.questionnaire.entity.Title;
import com.niuma.questionnaire.entity.User;
import com.niuma.questionnaire.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleServiceImpl implements TitleService {

    @Autowired
    TitleMapper titleMapper;

    @Override
    public List<Title> getTitle(User user) {
        QueryWrapper<Title> wrapper = new QueryWrapper<>();
        wrapper.eq("UID", user.getId());
        return titleMapper.selectList(wrapper);
    }
}
