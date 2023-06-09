package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.User;

import java.util.HashMap;

public interface UserService {

    Integer signup(User user);

    String login(HashMap<String, Object> map);
}
