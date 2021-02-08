package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.OrderItemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class ProductServiceFallback implements ProductService {

    @Override
    public List<OrderItemDto> getProductsByCodes(List<String> code) {
        log.info("Product service failed. Calling fallback and returning empty products list");
        return Collections.emptyList();
    }
}
