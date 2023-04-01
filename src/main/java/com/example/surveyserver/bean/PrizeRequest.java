package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrizeRequest {
    private Integer surveyId;
    private String name;
    private String description;
    private Integer quantity;
}
