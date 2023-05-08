package com.example.surveyserver.oauth2;

import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
@Profile({"development", "production"})
public class OAuth2Controller {

    @GetMapping("/sso")
    public PrincipalUser sso(Authentication authentication) {
        PrincipalUser principalUser = (PrincipalUser) authentication.getPrincipal();
        return principalUser;
    }

    @GetMapping("/me")
    public PrincipalUser getPrincipalUser(Authentication authentication) {
        return (PrincipalUser) authentication.getPrincipal();
    }

}