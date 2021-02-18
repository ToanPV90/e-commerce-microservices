package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.controller.dto.ProductDto;
import com.mikhailkarpov.products.controller.dto.ProductSearchParameters;
import com.mikhailkarpov.products.persistence.entity.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(ProductDto product);

    Product findProductByCode(String code);

    List<Product> findAll();

    List<Product> findByParameters(ProductSearchParameters parameters);

    Product updateProduct(String code, ProductDto update);

}
