package com.mikhailkarpov.customers.controller;

import com.mikhailkarpov.customers.dto.CustomerRegistrationRequest;
import com.mikhailkarpov.customers.service.AuthService;
import com.mikhailkarpov.customers.service.RegistrationService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @HystrixCommand
    @PostMapping("/registration")
    public void hello(@Valid @RequestBody CustomerRegistrationRequest registrationRequest) {
        log.info("Request to register a new customer: {}", registrationRequest);
        registrationService.registerCustomer(registrationRequest.getEmail(), registrationRequest.getPassword());
    }
}
