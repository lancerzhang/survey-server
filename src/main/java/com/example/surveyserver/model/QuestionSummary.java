package com.example.surveyserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuestionSummary {
    private int questionId;
    private String questionType;
    private String questionText;
    private Map<String, Integer> optionCounts;
    private Map<String, Double> optionPercentages;

    public QuestionSummary(Integer id, String questionType, String questionText, Map<String, Integer> optionCounts, Map<String, Double> optionPercentages) {
        this.questionId = id;
        this.questionType = questionType;
        this.questionText = questionText;
        this.optionCounts = optionCounts;
        this.optionPercentages = optionPercentages;
    }
}