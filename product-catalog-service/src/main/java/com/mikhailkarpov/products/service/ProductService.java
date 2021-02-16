package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Product findProductByCode(String code);

    List<Product> findProductsByCategoryId(Integer categoryId);

    List<Product> findProductsByCodesIn(List<String> codes);

    List<Product> searchProducts(String query);
}
