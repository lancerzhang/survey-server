package com.example.surveyserver.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/oauth2/")
@Profile("!development & !production")
public class OAuth2SimulatorController {

    @GetMapping("/me")
    public PrincipalUser getPrincipalUser() {
        return new PrincipalUser("01234567", "sampleUser", "sample.user@example.com");
    }

}
