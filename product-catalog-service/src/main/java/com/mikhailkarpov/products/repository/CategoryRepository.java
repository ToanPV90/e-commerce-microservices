package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    Iterable<Category> findAllByParentId(Integer id);

    Iterable<Category> findAllByParentName(String name);

    Optional<Category> findByName(String name);
}
