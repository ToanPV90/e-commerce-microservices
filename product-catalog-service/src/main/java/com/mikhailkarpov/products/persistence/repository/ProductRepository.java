package com.mikhailkarpov.products.persistence.repository;

import com.mikhailkarpov.products.persistence.entity.Product;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, String>, JpaSpecificationExecutor<Product> {

    boolean existsByCode(String code);

    List<Product> findAll();

    List<Product> findAllByCategoriesId(Integer id);

    List<Product> findAllByCodeIn(List<String> codes);

    List<Product> findAllByNameContainingIgnoreCase(String name);

    Optional<Product> findByCode(String code);
}
