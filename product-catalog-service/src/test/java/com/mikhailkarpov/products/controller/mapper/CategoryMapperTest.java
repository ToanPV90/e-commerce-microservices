package com.mikhailkarpov.products.controller.mapper;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.persistence.entity.Category;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryMapperTest {

    private final CategoryMapper mapper = new CategoryMapper();

    @Test
    void map() {
        //given
        Category category = new Category("name");
        category.setId(100);

        //then
        CategoryDto dto = mapper.map(category);
        assertEquals(100, dto.getId());
        assertEquals("name", dto.getName());
    }

}