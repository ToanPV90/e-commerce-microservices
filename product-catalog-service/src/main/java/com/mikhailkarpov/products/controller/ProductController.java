package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/categories/{id}/products")
    public List<ProductDto> findProductsByCategoryId(@PathVariable Integer id) {

        log.info("Request for products in category '{}'", id);
        return productService.findProductsByCategoryId(id);
    }

    @GetMapping("/products/{code}")
    public ProductDto findProductByCode(@PathVariable String code) {

        log.info("Request for product '{}'", code);
        return productService.findProductByCode(code);
    }

    @GetMapping("/products/list")
    public List<ProductDto> findProductsByCodesIn(@RequestParam("code") List<String> codes) {

        log.info("Request for products list: {}", codes);
        return productService.findProductsByCodesIn(codes);
    }

    @GetMapping("/products/search")
    public List<ProductDto> searchProducts(@RequestParam String query) {

        log.info("Request to search products '{}'", query);
        return productService.searchProducts(query);
    }
}
