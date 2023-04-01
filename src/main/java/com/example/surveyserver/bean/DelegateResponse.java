package com.example.surveyserver.bean;

import com.example.surveyserver.model.User;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class DelegateResponse {
    private Integer id;
    private User delegator;
    private User delegate;
}
