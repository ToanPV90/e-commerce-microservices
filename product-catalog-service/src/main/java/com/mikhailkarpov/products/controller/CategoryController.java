package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.controller.mapper.CategoryMapper;
import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/categories")
    public List<CategoryDto> findAll(@RequestParam(required = false, defaultValue = "false") Boolean includeSubdirectories) {

        log.info("Request for categories, including subcategories = {}", includeSubdirectories);

        return categoryService.findAll(includeSubdirectories)
                .stream()
                .map(categoryMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createCategory(@RequestParam String name,
                                                      UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create category '{}'", name);
        Category category = categoryService.createCategory(name);

        URI location = uriComponentsBuilder.path("/categories/{id}").build(category.getId());

        return ResponseEntity
                .created(location)
                .body(categoryMapper.map(category));
    }

    @GetMapping("/categories/{id}")
    public CategoryDto findCategoryById(@PathVariable Integer id) {

        log.info("Request for category with id = {}", id);
        Category category = categoryService.findById(id);

        return categoryMapper.map(category);
    }

    @PutMapping("/categories/{id}")
    public CategoryDto renameCategory(@PathVariable Integer id, @RequestParam String name) {

        log.info("Request for category with id = {}", id);
        Category category = categoryService.renameCategory(id, name);

        return categoryMapper.map(category);
    }

    @GetMapping("/categories/{id}/subcategories")
    public List<CategoryDto> findSubcategoriesByParentId(@PathVariable Integer id) {

        log.info("Request for subcategories in category with id = {}", id);
        return categoryService
                .findSubcategoriesByParentId(id)
                .stream()
                .map(categoryMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping("/categories/{id}/subcategories")
    public ResponseEntity<CategoryDto> createSubcategory(@PathVariable("id") Integer parentId,
                                                         @RequestParam("name") String subcategoryName,
                                                         UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create subcategory '{}' in category with id = {}", subcategoryName, parentId);
        Category subcategory = categoryService.createSubcategory(parentId, subcategoryName);

        URI location = uriComponentsBuilder.path("/categories/{id}").build(subcategory.getId());

        return ResponseEntity
                .created(location)
                .body(categoryMapper.map(subcategory));
    }

    @PostMapping("/categories/{id}/products")
    public void addProduct(@PathVariable("id") Integer categoryId, @RequestParam("code") String productCode) {

        log.info("Request to add product '{}' to category with id = {}", productCode, categoryId);
        categoryService.addProduct(categoryId, productCode);
    }

    @DeleteMapping("/categories/{id}/products/{code}")
    public void removeProduct(@PathVariable("id") Integer categoryId, @PathVariable("code") String productCode) {

        log.info("Request to remove product '{}' from category with id = {}", productCode, categoryId);
        categoryService.removeProduct(categoryId, productCode);
    }

    @PostMapping("/categories/{id}/move")
    public void moveCategory(@PathVariable Integer id, @RequestParam("destination-id") Integer destinationId) {

        log.info("Request to move category with id = {} to category with id = {}", id, destinationId);
        categoryService.moveCategory(id, destinationId);
    }
}
