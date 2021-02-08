package com.mikhailkarpov.shoppingcart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    @NotBlank
    private String host;

    @Value("${spring.redis.port}")
    @NotNull
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);
        redisConfig.setPassword(password);

        return new LettuceConnectionFactory(redisConfig);
    }
}
