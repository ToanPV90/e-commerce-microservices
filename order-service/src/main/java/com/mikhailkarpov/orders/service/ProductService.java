package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.config.ProductServiceConfig;
import com.mikhailkarpov.orders.dto.ProductDto;
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
    List<ProductDto> getProductsByCodes(@RequestParam List<String> code);
}
