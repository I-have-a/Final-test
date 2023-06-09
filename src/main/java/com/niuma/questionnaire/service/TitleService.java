package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Title;
import com.niuma.questionnaire.entity.User;

import java.util.List;

public interface TitleService {
    List<Title> getTitle(User user);
}
