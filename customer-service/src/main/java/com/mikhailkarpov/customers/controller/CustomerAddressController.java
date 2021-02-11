package com.mikhailkarpov.customers.controller;

import com.mikhailkarpov.customers.dto.AddressDto;
import com.mikhailkarpov.customers.service.AddressService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequestMapping("/customers/me/address")
@PreAuthorize("hasRole('CUSTOMER')")
@AllArgsConstructor
public class CustomerAddressController {

    private final AddressService addressService;

    @PostMapping
    public AddressDto createAddress(@Valid @RequestBody AddressDto address,
                                    @AuthenticationPrincipal Principal principal) {

        log.info("Request from {} to create address: {}", principal.getName(), address);
        return addressService.createAddress(principal.getName(), address);
    }

    @GetMapping
    public AddressDto getAddress(@AuthenticationPrincipal Principal principal) {

        log.info("Request from {} for his address", principal.getName());
        return addressService.findAddressByCustomerName(principal.getName());
    }

    @PutMapping
    public AddressDto updateAddress(@Valid @RequestBody AddressDto update,
                                    @AuthenticationPrincipal Principal principal) {

        log.info("Request from {} to update address: {}", principal.getName(), update);
        return addressService.updateAddress(principal.getName(), update);
    }
}
