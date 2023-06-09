package com.niuma.questionnaire.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuma.questionnaire.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface TestMapper extends BaseMapper<Test> {
}
