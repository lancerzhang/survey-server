package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {
    private Integer id;
    private String username;
    private boolean anonymous;
    private String publicKey;
    private String email;
    private String createdAt;
    private String lastModified;
}
