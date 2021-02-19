package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.persistence.entity.Category;

import java.util.List;

public interface CategoryService {

    Category createCategory(String name);

    Category createSubcategory(Integer parentId, String name);

    Category findById(Integer id);

    List<Category> findAll(boolean includeSubcategories);

    List<Category> findSubcategoriesByParentId(Integer id);

    void moveCategory(Integer id, Integer destinationId);

    Category renameCategory(Integer id, String name);

    void addProduct(Integer categoryId, String productCode);

    void removeProduct(Integer categoryId, String productCode);
}
