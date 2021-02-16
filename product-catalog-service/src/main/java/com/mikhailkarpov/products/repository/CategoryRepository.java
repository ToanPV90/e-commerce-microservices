package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

    List<Category> findAllByParentId(Integer id);
}
