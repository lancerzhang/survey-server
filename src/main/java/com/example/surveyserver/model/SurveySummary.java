package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
public class SurveySummary {
    private int totalReplies;
    private List<QuestionSummary> questionSummaries;

    public SurveySummary(int totalReplies, List<QuestionSummary> questionSummaries) {
        this.totalReplies = totalReplies;
        this.questionSummaries = questionSummaries;
    }
}