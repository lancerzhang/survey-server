package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyReplyRequest {
    private Integer userId;
    private Integer surveyId;
    private List<QuestionReplyRequest> questionResponses;
}
