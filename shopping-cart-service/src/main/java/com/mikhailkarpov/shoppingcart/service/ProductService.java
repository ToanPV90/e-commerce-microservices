package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.config.ProductServiceConfig;
import com.mikhailkarpov.shoppingcart.domain.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        value = "product-catalog-service",
        configuration = ProductServiceConfig.class,
        fallback = ProductServiceFallback.class)
public interface ProductService {

    @GetMapping("/products/list")
    List<Product> getProductsByCodes(@RequestParam List<String> code);
}
