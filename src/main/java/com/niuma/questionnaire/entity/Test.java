package com.niuma.questionnaire.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.xml.crypto.Data;
import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    Long id;
    Data startTime;
    Data endTime;
    String title;
    int status;
    @TableField(exist = false)
    List<Title> titles;
}
