package com.mikhailkarpov.products.controller.mapper;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.persistence.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements EntityToDtoMapper<Category, CategoryDto> {

    @Override
    public CategoryDto map(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}
