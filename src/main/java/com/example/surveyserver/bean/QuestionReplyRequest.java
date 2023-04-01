package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionReplyRequest {
    private Integer questionId;
    private String responseText;
    private List<OptionReplyRequest> optionResponses;
}
