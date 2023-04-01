package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;
import com.example.surveyserver.model.Question.QuestionType;

import java.util.List;

@Getter
@Setter
public class QuestionRequest {
    private Integer surveyId;
    private String questionText;
    private QuestionType questionType;
    private List<OptionRequest> options;
}
