package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.dto.CustomerDto;

import java.util.UUID;

public interface CustomerService {

    CustomerDto findCustomerById(UUID id);
}
