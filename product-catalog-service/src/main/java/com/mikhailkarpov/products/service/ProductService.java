package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;

import java.util.Arrays;
import java.util.List;

public interface ProductService {

    Product createProduct(ProductDto product);

    Product findProductByCode(String code);

    List<Product> findAll();

    List<Product> findProductsByCategoryId(Integer categoryId);

    List<Product> findProductsByCodesIn(List<String> codes);

    List<Product> searchProducts(String query);

    Product updateProduct(String code, ProductDto update);

    List<Product> findAll(String name, List<String> codes);
}
