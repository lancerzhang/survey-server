package com.example.surveyserver.oauth2;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrincipalUser {

    private final String employeeId;
    private final String displayName;
    private final String email;

    public PrincipalUser(String employeeId, String displayName, String email) {
        this.employeeId = employeeId;
        this.displayName = displayName;
        this.email = email;
    }

    @Override
    public String toString() {
        return "PrincipalUser{" +
                "employeeId='" + employeeId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
