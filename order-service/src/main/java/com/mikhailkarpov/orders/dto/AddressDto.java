package com.mikhailkarpov.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    @NotBlank
    private String zip;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    private String street;
}
