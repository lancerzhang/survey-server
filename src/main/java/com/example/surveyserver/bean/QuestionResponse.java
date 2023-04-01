package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;
import com.example.surveyserver.model.Question.QuestionType;

@Getter
@Setter
public class QuestionResponse {
    private Integer id;
    private String questionText;
    private QuestionType questionType;
}
