package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(ProductDto product);

    ProductDto updateProduct(String code, ProductDto update);

    ProductDto findProductByCode(String code);

    List<ProductDto> findAllProducts();

    void deleteProduct(String code);

    List<ProductDto> search(String query);

}
