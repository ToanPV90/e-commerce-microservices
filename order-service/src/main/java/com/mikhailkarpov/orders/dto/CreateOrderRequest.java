package com.mikhailkarpov.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotNull
    @NotEmpty
    private Set<CreateOrderItemRequest> items;

    @NotNull
    @Valid
    private AddressDto address;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateOrderItemRequest {

        @NotBlank
        private String code;

        @NotNull
        @Min(1)
        private Integer amount;
    }
}
