package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Test;
import com.niuma.questionnaire.entity.User;

import java.util.List;

public interface TestService {
    List<Test> getTest(String userID);
}
