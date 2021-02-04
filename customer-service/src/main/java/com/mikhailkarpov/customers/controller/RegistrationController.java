package com.mikhailkarpov.customers.controller;

import com.mikhailkarpov.customers.dto.CustomerRegistrationRequest;
import com.mikhailkarpov.customers.service.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/registration")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void hello(@RequestBody CustomerRegistrationRequest registrationRequest) {
        log.info("Request to register a new customer: {}", registrationRequest);
        registrationService.registerCustomer(registrationRequest.getEmail(), registrationRequest.getPassword());
    }
}
