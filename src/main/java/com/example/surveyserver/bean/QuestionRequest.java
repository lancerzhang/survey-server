package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;
import com.example.surveyserver.model.Question.QuestionType;

@Getter
@Setter
public class QuestionRequest {
    private Integer surveyId;
    private String questionText;
    private QuestionType questionType;
}
