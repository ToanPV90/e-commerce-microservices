package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.persistence.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category createCategory(String name) {

        Category category = categoryRepository.save(new Category(name));

        log.info("Saving category {}", category);
        return category;
    }

    @Override
    @Transactional(readOnly = true)
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
            categoryRepository.findAllByParentId(null).forEach(categories::add);
        }

        return categories;
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

        if (!category.move(destination)) {
            String moving_category_failed = String.format("Moving %s to %s failed", category, destination);
            throw new RuntimeException(moving_category_failed);
        }
    }

    @Override
    @Transactional
    public Category renameCategory(Integer id, String name) {

        Category category = findById(id);
        category.setName("name");

        log.info("Renaming {}", category);
        return category;
    }
}
