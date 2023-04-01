package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class SurveyResponse {
    private Integer id;
    private String title;
    private String description;
    private Boolean allowAnonymousResponse;
    private Boolean allowResubmit;
    private Boolean isDeleted;
    private Timestamp lastModified;
    private Timestamp startTime;
    private Timestamp endTime;
    private Integer maxResponses;
    private String createdAt;
}
