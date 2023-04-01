package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionReplyResponse {
    private Integer optionId;
    private String optionText;
    private boolean selected;
}
