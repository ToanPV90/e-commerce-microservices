package com.mikhailkarpov.products.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchParameters {

    private String name;

    private List<String> codes;

    private Integer categoryId;
}
