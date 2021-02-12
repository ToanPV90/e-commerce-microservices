package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.CategoryDto;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto category);

    void deleteCategory(Long id);

    Iterable<CategoryDto> findAll();

    CategoryDto findById(Long id);

    CategoryDto updateCategory(Long id, CategoryDto update);

}
