package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyReplySummary {
    private String surveyTitle;
    private int totalReplies;
    private List<QuestionReplySummary> questionSummaries;

    public SurveyReplySummary(String surveyTitle, int totalReplies, List<QuestionReplySummary> questionSummaries) {
        this.surveyTitle = surveyTitle;
        this.totalReplies = totalReplies;
        this.questionSummaries = questionSummaries;
    }
}