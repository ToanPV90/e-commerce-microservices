package com.mikhailkarpov.products.controller.mapper;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.persistence.entity.Product;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductMapperTest {

    private final ProductMapper productMapper = new ProductMapper();

    @Test
    void map() {
        //given
        Product product = new Product("abc", "name", "desc", 1000, 100);

        //then
        ProductDto dto = productMapper.map(product);
        assertEquals("abc", dto.getCode());
        assertEquals("name", dto.getName());
        assertEquals("desc", dto.getDescription());
        assertEquals(1000, dto.getPrice());
        assertEquals(100, dto.getAmount());
    }
}