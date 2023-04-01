package com.example.surveyserver.bean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
    private String username;
    private String password;
    private boolean anonymous;
    private String publicKey;
    private String email;
}
