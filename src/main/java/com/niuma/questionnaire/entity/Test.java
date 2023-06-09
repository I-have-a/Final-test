package com.niuma.questionnaire.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Test {
    Long id;
    @TableField(value = "starttime")
    Date startTime;
    @TableField(value = "endtime")
    Date endTime;
    String title;
    int status;
    @TableField(exist = false)
    List<Title> titles;
}
