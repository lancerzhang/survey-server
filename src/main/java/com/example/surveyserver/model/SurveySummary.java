package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveySummary {
    private String surveyTitle;
    private int totalReplies;
    private List<QuestionSummary> questionSummaries;

    public SurveySummary(String surveyTitle, int totalReplies, List<QuestionSummary> questionSummaries) {
        this.surveyTitle = surveyTitle;
        this.totalReplies = totalReplies;
        this.questionSummaries = questionSummaries;
    }
}