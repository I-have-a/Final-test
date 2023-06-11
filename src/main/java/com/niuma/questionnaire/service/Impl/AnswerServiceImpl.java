package com.niuma.questionnaire.service.Impl;

import com.niuma.questionnaire.dao.AnswerMapper;
import com.niuma.questionnaire.entity.Answer;
import com.niuma.questionnaire.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    AnswerMapper answerMapper;

    @Override
    public List<Answer> getAnswer(HashMap<String, Object> map) {
        return answerMapper.getAnswerByTest(map);
    }
}
