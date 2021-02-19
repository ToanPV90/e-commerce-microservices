package com.mikhailkarpov.products.controller.mapper;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.persistence.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper implements EntityToDtoMapper<Product, ProductDto> {

    @Override
    public ProductDto map(Product product) {
        return ProductDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }
}
