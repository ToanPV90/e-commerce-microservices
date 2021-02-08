package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.config.AuthServiceConfig;
import com.mikhailkarpov.customers.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "auth-service", configuration = AuthServiceConfig.class)
public interface AuthService {

    @PostMapping("/users")
    UserDto createUser(@RequestBody UserDto user);
}
