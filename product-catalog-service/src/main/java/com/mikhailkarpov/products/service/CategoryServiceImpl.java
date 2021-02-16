package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.entity.Category;
import com.mikhailkarpov.products.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> findParentCategories() {

        return categoryRepository.findAllByParentId(null);
    }

    @Override
    public List<Category> findSubcategoriesByParentId(Integer id) {

        return categoryRepository.findAllByParentId(id);
    }
}
