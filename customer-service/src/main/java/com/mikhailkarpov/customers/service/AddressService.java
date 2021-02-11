package com.mikhailkarpov.customers.service;

import com.mikhailkarpov.customers.dto.AddressDto;

public interface AddressService {

    AddressDto createAddress(String customerName, AddressDto address);

    AddressDto findAddressByCustomerName(String customerName);

    AddressDto updateAddress(String customerName, AddressDto update);

}
