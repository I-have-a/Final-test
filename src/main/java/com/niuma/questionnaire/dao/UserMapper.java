package com.niuma.questionnaire.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.niuma.questionnaire.entity.User;
import org.apache.ibatis.annotations.Mapper;

import javax.annotation.Resource;

@Resource
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
