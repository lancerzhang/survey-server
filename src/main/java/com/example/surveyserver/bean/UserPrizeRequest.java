package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPrizeRequest {
    private Integer userId;
    private Integer prizeId;
}
