package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.OrderItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "product-catalog-service")
public interface ProductServiceClient {

    @GetMapping("/products/list")
    List<OrderItemDto> getProductsByCodes(@RequestParam List<String> code);
}
