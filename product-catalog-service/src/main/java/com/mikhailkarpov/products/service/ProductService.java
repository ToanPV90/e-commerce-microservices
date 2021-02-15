package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    ProductDto findProductByCode(String code);

    List<ProductDto> findProductsByCategoryId(Integer categoryId);

    List<ProductDto> findProductsByCodesIn(List<String> codes);

    List<ProductDto> searchProducts(String query);
}
