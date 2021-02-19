package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.persistence.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductDto product);

    Product findProductByCode(String code);

    List<Product> findAll();

    List<Product> findBySpecification(Specification<Product> specification);

    Product updateProduct(String code, ProductDto update);

}
