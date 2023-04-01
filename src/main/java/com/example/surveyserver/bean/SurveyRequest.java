package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
public class SurveyRequest {
    private Integer userId;
    private String title;
    private String description;
    private Boolean allowAnonymousResponse;
    private Boolean allowResubmit;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer maxResponses;

    private List<QuestionRequest> questions;
}
