package com.niuma.questionnaire.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Title {
    Long id;
    String content;
    Type type;
    @TableField(exist = false)
    List<Option> options;
    @TableField(exist = false)
    List<Answer> answers;
}
