package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionReplyResponse {
    private Integer questionId;
    private String questionTitle;
    private String questionDescription;
    private String responseText;
    private List<OptionReplyResponse> optionResponses;
}
