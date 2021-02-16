package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findParentCategories();

    List<Category> findSubcategoriesByParentId(Integer id);
}
