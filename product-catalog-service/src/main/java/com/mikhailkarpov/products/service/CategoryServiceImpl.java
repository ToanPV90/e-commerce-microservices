package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public Category createCategory(String name) {

        Category category = categoryRepository.save(new Category(name));

        log.info("Saving category {}", category);
        return category;
    }

    @Override
    @Transactional
    public Category createSubcategory(Integer parentId, String name) {

        Category parent = findById(parentId);

        Category subcategory = parent.createSubcategory(name);
        categoryRepository.save(subcategory);

        log.info("Saving subcategory {}", subcategory);
        return subcategory;
    }

    @Override
    @Transactional
    public Category findById(Integer id) {

        return categoryRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Category with id = %d not found", id);
            return new ResourceNotFoundException(message);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll(boolean includeSubcategories) {

        List<Category> categories = new ArrayList<>();

        if (includeSubcategories) {
            categoryRepository.findAll().forEach(categories::add);
        } else {
            categories = categoryRepository.findAllByParentId(null);
        }

        return Collections.unmodifiableList(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> findSubcategoriesByParentId(Integer id) {

        return categoryRepository.findAllByParentId(id);
    }

    @Override
    @Transactional
    public void moveCategory(Integer id, Integer destinationId) {

        Category category = findById(id);
        Category destination = findById(destinationId);

        category.move(destination);
    }

    @Override
    @Transactional
    public Category renameCategory(Integer id, String name) {

        Category category = findById(id);
        category.setName(name);

        log.info("Renaming {}", category);
        return category;
    }

    @Override
    @Transactional
    public void addProduct(Integer categoryId, String productCode) {

        Product product = productService.findProductByCode(productCode);

        Category category = findById(categoryId);
        category.addProduct(product);
    }

    @Override
    @Transactional
    public void removeProduct(Integer categoryId, String productCode) {

        Product product = productService.findProductByCode(productCode);

        Category category = findById(categoryId);
        category.removeProduct(product);
    }
}
