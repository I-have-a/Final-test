package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Answer;

import java.util.HashMap;
import java.util.List;

public interface AnswerService {
    List<Answer> getAnswer(HashMap<String, Object> map);
}
