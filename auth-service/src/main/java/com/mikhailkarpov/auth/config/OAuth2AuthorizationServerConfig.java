package com.mikhailkarpov.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore(); // todo tokens persistence
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //todo clients persistence
        //@formatter:off
        clients.inMemory()
                .withClient("web-client")
                    .secret(passwordEncoder.encode("web-client-secret"))
                    .authorizedGrantTypes("password")
                    .scopes("web")
                    .and()
                .withClient("customer-service")
                    .secret(passwordEncoder.encode("customer-service-secret"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("server")
                    .and()
                .withClient("product-catalog-service")
                    .secret(passwordEncoder.encode("product-catalog-service-secret"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("server")
                    .and()
                .withClient("order-service")
                    .secret(passwordEncoder.encode("order-service-secret"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("server")
                    .and()
                .withClient("admin")
                    .secret(passwordEncoder.encode("admin-secret"))
                    .authorizedGrantTypes("client_credentials")
                    .authorities("ROLE_ADMIN")
                    .scopes("admin");
        //@formatter:on
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenStore(tokenStore());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder);
    }
}
