package com.example.surveyserver.oauth2;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2SsoDefaultConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;

@EnableOAuth2Sso
@Profile({"development", "production"})
@Order(6)
public class OAuth2SsoConfig extends OAuth2SsoDefaultConfiguration {

    public OAuth2SsoConfig(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        super.configure(http);
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.cors().disable();
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().antMatchers("/.well-known/**", "/oauth2/**");
    }

}
