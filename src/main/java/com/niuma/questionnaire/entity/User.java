package com.niuma.questionnaire.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long id;
    @TableField(exist = false)
    List<Answer> answers;
    @TableField(exist = false)
    List<Test> tests;
    @TableField(exist = false)
    List<Title> titles;
}
