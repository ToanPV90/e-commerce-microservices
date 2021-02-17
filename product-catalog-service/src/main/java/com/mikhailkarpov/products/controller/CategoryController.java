package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.entity.Category;
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

    @GetMapping("/categories")
    public List<CategoryDto> findParentCategories(@RequestParam(required = false, defaultValue = "false") Boolean includeSubdirectories) {

        log.info("Request for parent categories");
        List<Category> categories = includeSubdirectories ?
                categoryService.findAll() :
                categoryService.findParentCategories();

        return categories
                .stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryDto> createParentCategory(@RequestParam String name,
                                                            UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create parent category '{}'", name);
        Category category = categoryService.createCategory(name);

        URI location = uriComponentsBuilder.path("/categories/{id}").build(category.getId());

        return ResponseEntity
                .created(location)
                .body(mapDto(category));
    }

    @GetMapping("/categories/{id}")
    public CategoryDto findCategoryById(@PathVariable Integer id) {

        log.info("Request for category with id = {}", id);
        Category category = categoryService.findById(id);

        return mapDto(category);
    }

    @PutMapping("/categories/{id}")
    public CategoryDto renameCategory(@PathVariable Integer id, @RequestParam String name) {

        log.info("Request for category with id = {}", id);
        Category category = categoryService.renameCategory(id, name);

        return mapDto(category);
    }

    @GetMapping("/categories/{id}/subcategories")
    public List<CategoryDto> findSubcategoriesByParentId(@PathVariable Integer id) {

        log.info("Request for subcategories in category with id = {}", id);
        return categoryService
                .findSubcategoriesByParentId(id)
                .stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/categories/{id}/subcategories")
    public ResponseEntity<CategoryDto> createSubcategory(@PathVariable("id") Integer parentId,
                                                        @RequestParam String name,
                                                        UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create subcategory '{}' in category with id = {}", name, parentId);
        Category category = categoryService.createSubcategory(parentId, name);

        URI location = uriComponentsBuilder.path("/categories/{id}").build(category.getId());

        return ResponseEntity
                .created(location)
                .body(mapDto(category));
    }

    @PostMapping("/categories/{id}/move")
    public void moveCategory(@PathVariable Integer id, @RequestParam("destination-id") Integer destinationId) {

        log.info("Request to move category with id = {} to category with id = {}", id, destinationId);
        categoryService.moveCategory(id, destinationId);
    }

    private CategoryDto mapDto(Category category) {

        return new CategoryDto(category.getId(), category.getName());
    }
}
