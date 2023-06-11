package com.niuma.questionnaire.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.niuma.questionnaire.dao.OptionMapper;
import com.niuma.questionnaire.entity.Option;
import com.niuma.questionnaire.service.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OptionServiceImpl implements OptionService {

    @Autowired
    OptionMapper optionMapper;

    @Override
    public List<Option> testOption(HashMap<String, Option> map) {
        return optionMapper.getOptionByTestID(map);
    }

    @Override
    public List<Option> titleOption(HashMap<String, Object> map) {
        QueryWrapper<Option> wrapper = new QueryWrapper<>();
        wrapper.eq("TID",map.get("TiID")).select("id","content","TID");
        return optionMapper.selectList(wrapper);
    }
}
