package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionReplyRequest {
    private Integer optionId;
    private boolean selected;
}
