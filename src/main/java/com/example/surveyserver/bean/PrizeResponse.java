package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrizeResponse {
    private Integer id;
    private String name;
    private String description;
    private Integer quantity;
}
