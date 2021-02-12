package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.entity.Category;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto category) {

        String name = category.getName();

        if (categoryRepository.existsByName(name)) {
            String message = String.format("Category '%s' already exists", name);
            throw new ResourceAlreadyExistsException(message);
        }

        Category created = categoryRepository.save(new Category(name));
        log.info("Creating {}", created);

        return mapDtoFromEntity(created);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {

        Category category = getCategoryOrElseThrow(id);
        log.info("Deleting {}", category);
        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<CategoryDto> findAll() {

        List<Category> categories = new LinkedList<>();
        categoryRepository.findAll().forEach(categories::add);
        log.info("Found categories: {}", categories.size());

        return categories.stream()
                .map(this::mapDtoFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {

        Category category = getCategoryOrElseThrow(id);
        log.info("Found {}", category);

        return mapDtoFromEntity(category);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto update) {

        Category category = getCategoryOrElseThrow(id);
        category.setName(update.getName());
        log.info("Updating {}", category);

        return mapDtoFromEntity(category);
    }

    private Category getCategoryOrElseThrow(Long id) {

        return categoryRepository.findById(id).orElseThrow(() -> {
            String message = String.format("Category with id '%d' not found", id);
            return new ResourceNotFoundException(message);
        });
    }

    private CategoryDto mapDtoFromEntity(Category category) {

        return new CategoryDto(category.getId(), category.getName());
    }
}
