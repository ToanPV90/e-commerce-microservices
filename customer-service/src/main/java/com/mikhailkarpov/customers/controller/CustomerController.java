package com.mikhailkarpov.customers.controller;

import com.mikhailkarpov.customers.dto.CustomerDto;
import com.mikhailkarpov.customers.service.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}")
    public CustomerDto findCustomerById(@PathVariable UUID id) {
        log.info("Request for a customer with id = {}", id);
        return customerService.findCustomerById(id);
    }
}
