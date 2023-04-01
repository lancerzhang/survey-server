package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OptionRequest {
    private Integer questionId;
    private String optionText;
}
