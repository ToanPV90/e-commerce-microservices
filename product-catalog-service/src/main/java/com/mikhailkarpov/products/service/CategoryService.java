package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> findParentCategories();

    List<CategoryDto> findAllByParentId(Integer id);
}
