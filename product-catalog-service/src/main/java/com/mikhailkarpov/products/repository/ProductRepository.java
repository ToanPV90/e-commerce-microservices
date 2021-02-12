package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends CrudRepository<Product, UUID> {

    boolean existsByCode(String code);

    Optional<Product> findByCode(String code);

    List<Product> findAllByNameContaining(String name);

    List<Product> findAllByCodeIn(List<String> codes);

    List<Product> findAllByCategoryId(Long categoryId);
}
