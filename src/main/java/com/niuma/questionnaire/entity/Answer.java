package com.niuma.questionnaire.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    Long id;
    String content;
    Long tID;
    Option option;
}
