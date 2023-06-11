package com.niuma.questionnaire.service;

import com.niuma.questionnaire.entity.Option;

import java.util.HashMap;
import java.util.List;

public interface OptionService {
    List<Option> testOption(HashMap<String,Option> map);

    List<Option> titleOption(HashMap<String,Object> map);
}
