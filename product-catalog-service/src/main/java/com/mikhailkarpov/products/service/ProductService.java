package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDto createProduct(Long categoryId, ProductDto product);

    ProductDto updateProduct(String code, ProductDto update);

    ProductDto findProductByCode(String code);

    Iterable<ProductDto> findAllByCodes(List<String> codes);

    void deleteProduct(String code);

    Iterable<ProductDto> search(String query);

    Iterable<ProductDto> findAllByCategoryId(Long categoryId);
}
