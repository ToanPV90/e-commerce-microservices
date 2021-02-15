package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> findParentCategories() {

        log.info("Request for parent categories");
        return categoryService.findParentCategories();
    }

    @GetMapping("/categories/{id}/subcategories")
    public List<CategoryDto> findSubcategoriesByParentId(@PathVariable Integer id) {

        log.info("Request for subcategories in category with id = {}", id);
        return categoryService.findAllByParentId(id);
    }
}
