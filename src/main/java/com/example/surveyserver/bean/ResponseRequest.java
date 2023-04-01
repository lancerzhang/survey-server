package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseRequest {
    private Integer userId;
    private Integer surveyId;
    private Integer questionId;
    private Integer optionId;
    private String responseText;
}
