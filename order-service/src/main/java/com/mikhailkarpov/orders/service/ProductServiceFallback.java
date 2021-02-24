package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductServiceFallback implements ProductService {

    @Override
    public List<ProductDto> getProductsByCodes(List<String> codes) {
        log.warn("Product service failed. Calling fallback and returning empty products list");
        return Collections.emptyList();
    }
}
