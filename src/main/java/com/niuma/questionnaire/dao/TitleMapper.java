package com.niuma.questionnaire.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuma.questionnaire.entity.Title;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface TitleMapper extends BaseMapper<Title> {
    List<Title> getTitleByTestID(HashMap<String,Object> map);
}
