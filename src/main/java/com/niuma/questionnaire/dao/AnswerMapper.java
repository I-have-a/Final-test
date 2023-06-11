package com.niuma.questionnaire.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuma.questionnaire.entity.Answer;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface AnswerMapper extends BaseMapper<Answer> {
    List<Answer> getAnswerByTest(HashMap<String,Object> map);
}
