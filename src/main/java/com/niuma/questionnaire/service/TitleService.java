package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Title;

import java.util.HashMap;
import java.util.List;

public interface TitleService {

    /**
     * 获得用户创建的题目
     * @return
     */
    List<Title> getTitle(HashMap<String,Object> map);
}
