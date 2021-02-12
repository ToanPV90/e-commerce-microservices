package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto category) {

        log.info("Request for creating category: {}", category);
        return categoryService.createCategory(category);
    }

    @GetMapping("/categories")
    public Iterable<CategoryDto> findAll() {

        log.info("Request for all categories");
        return categoryService.findAll();
    }

    @GetMapping("/categories/{id}")
    public CategoryDto findByCategoryId(@PathVariable Long id) {

        log.info("Request for category '{}'", id);
        return categoryService.findById(id);
    }

    @PutMapping("/categories/{id}")
    public CategoryDto updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto update) {

        log.info("Request to update category {}", update);

        if (!id.equals(update.getId())) {
            throw new BadRequestException("URI id and category id don't match");
        }

        return categoryService.updateCategory(id, update);
    }

    @DeleteMapping("/categories/{id}")
    public void deleteCategory(@PathVariable Long id) {

        log.info("Request to delete category '{}'", id);
        categoryService.deleteCategory(id);
    }
}
