package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Title;

import java.util.HashMap;
import java.util.List;

public interface TitleService {
    List<Title> getUserTitle(String userID);

    List<Title> getTestTitle(HashMap<String,Object> map);
}
