package com.example.surveyserver.bean;

import com.example.surveyserver.model.Survey;
import com.example.surveyserver.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyReplyResponse {
    private Integer id;
    private User user;
    private Survey survey;
    private List<QuestionReplyResponse> questionResponses;
}
