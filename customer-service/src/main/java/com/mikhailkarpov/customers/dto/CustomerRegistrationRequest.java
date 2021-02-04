package com.mikhailkarpov.customers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationRequest {

    @NotBlank
    @Size(min = 3)
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @Override
    public String toString() {
        return "CustomerRegistrationRequest{" +
                "email='" + email + '\'' +
                ", password=" + "'[PROTECTED]'" +
                '}';
    }
}
