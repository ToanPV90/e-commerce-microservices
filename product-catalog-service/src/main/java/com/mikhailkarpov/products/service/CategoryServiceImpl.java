package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.CategoryDto;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final EntityManager entityManager;

    @Override
    public List<CategoryDto> findParentCategories() {

        return entityManager.createQuery(
                "SELECT new com.mikhailkarpov.products.dto.CategoryDto(c.id, c.name) " +
                        "FROM Category c " +
                        "WHERE c.parent = null", CategoryDto.class)
                .getResultList();
    }

    @Override
    public List<CategoryDto> findAllByParentId(Integer id) {

        List<CategoryDto> subcategories = entityManager.createQuery(
                "SELECT new com.mikhailkarpov.products.dto.CategoryDto(c.id, c.name) " +
                        "FROM Category c " +
                        "WHERE c.parent.id = :id", CategoryDto.class)
                .setParameter("id", id)
                .getResultList();

        if (subcategories.isEmpty() && !existsById(id)) {
            String message = String.format("Category with id = %d not found", id);
            throw new ResourceNotFoundException(message);
        }

        return subcategories;
    }

    private boolean existsById(Integer id) {
        Long count = entityManager
                .createQuery("COUNT(c) FROM Category c WHERE c.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();

        return count == 1;
    }
}
