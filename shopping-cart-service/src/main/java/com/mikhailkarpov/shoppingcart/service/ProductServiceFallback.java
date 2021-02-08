package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.domain.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductServiceFallback implements ProductService {

    @Override
    public List<Product> getProductsByCodes(List<String> codes) {
        log.warn("Calling fallback getProductsByCodes()");
        return codes.stream()
                .map(code -> new Product(code, "Product name not available", 0, 0))
                .collect(Collectors.toList());
    }
}
