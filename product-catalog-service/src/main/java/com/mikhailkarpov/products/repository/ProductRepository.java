package com.mikhailkarpov.products.repository;

import com.mikhailkarpov.products.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, String> {

    boolean existsByCode(String code);

    List<Product> findAllByCategoriesId(Integer id);

    List<Product> findAllByCodeIn(List<String> codes);

    List<Product> findAllByNameContainingIgnoreCase(String name);

    Optional<Product> findByCode(String code);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%')) AND p.code IN :codes")
    List<Product> search(@Param("name") String name, @Param("codes") List<String> codes);
}
