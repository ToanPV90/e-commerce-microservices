package com.mikhailkarpov.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //@formatter:off
        http
            .csrf().disable()
            .requestMatchers().antMatchers("/users/**")
            .and()
            .authorizeRequests().anyRequest().authenticated();
        //@formatter:on
    }
}
