package com.mikhailkarpov.orders.controller;

import com.mikhailkarpov.orders.dto.CreateOrderRequest;
import com.mikhailkarpov.orders.dto.OrderDto;
import com.mikhailkarpov.orders.entity.OrderStatus;
import com.mikhailkarpov.orders.exception.BadRequestException;
import com.mikhailkarpov.orders.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody CreateOrderRequest request,
                                                UriComponentsBuilder uriComponentsBuilder) {
        log.info("Request to create order: {}", request);
        OrderDto created = orderService.createOrder(request);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/orders/{id}").build(created.getId()))
                .body(created);
    }

    @GetMapping("/{id}")
    public OrderDto findOrderById(@PathVariable UUID id) {
        log.info("Request for order with id={}", id);
        return orderService.findOrderById(id);
    }

    @PutMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable UUID id, @RequestParam String status) {
        log.info("Request to update order status with id={}: {}", id, status);

        try {
            OrderStatus update = OrderStatus.valueOf(status.toUpperCase());
            return orderService.updateStatus(id, update);

        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Illegal status: " + status);
        }
    }

    @GetMapping("/account/{accountId}")
    public List<OrderDto> findOrdersByAccountId(@PathVariable String accountId) {
        log.info("Request for orders by account_id={}", accountId);
        return orderService.findOrdersByAccountId(accountId);
    }
}
