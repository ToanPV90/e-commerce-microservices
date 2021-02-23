package com.mikhailkarpov.shoppingcart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

    @Value("${spring.redis.host}")
    @NotBlank
    private String host;

    @Value("${spring.redis.port}")
    @NotNull
    private Integer port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(host);
        redisConfig.setPort(port);

        return new LettuceConnectionFactory(redisConfig);
    }
}
