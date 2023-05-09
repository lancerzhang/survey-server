package com.example.surveyserver.oauth2;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Component
public class UserConverter extends DefaultUserAuthenticationConverter {

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication authentication = super.extractAuthentication(map);
        PrincipalUser dummyPrincipal = new PrincipalUser(1, (String) map.get("employeeId"), (String) map.get("displayName"), (String) map.get("email"));
        dummyPrincipal.setDelegators(Collections.singletonList(1));
        return new UsernamePasswordAuthenticationToken(dummyPrincipal, null, null);
    }

}
