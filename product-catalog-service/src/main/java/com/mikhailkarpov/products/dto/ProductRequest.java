package com.mikhailkarpov.products.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest extends ProductDto {

    @NotNull
    @Size(min = 1)
    private Long categoryId;
}
