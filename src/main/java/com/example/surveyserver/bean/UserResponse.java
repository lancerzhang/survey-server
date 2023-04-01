package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String createdAt;
}
