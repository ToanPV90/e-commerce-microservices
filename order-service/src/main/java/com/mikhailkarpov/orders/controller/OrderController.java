package com.mikhailkarpov.orders.controller;

import com.mikhailkarpov.orders.dto.CreateOrderRequest;
import com.mikhailkarpov.orders.dto.OrderDto;
import com.mikhailkarpov.orders.entity.OrderStatus;
import com.mikhailkarpov.orders.exception.BadRequestException;
import com.mikhailkarpov.orders.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
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
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
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

    @GetMapping
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    public List<OrderDto> findOrdersByAccountId(@RequestParam String accountId) {
        log.info("Request for orders by account_id={}", accountId);
        return orderService.findOrdersByAccountId(accountId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<OrderDto> findOrdersForCurrentCustomer(@AuthenticationPrincipal Principal principal) {
        log.info("Requests for orders by customer {}", principal.getName());
        return orderService.findOrdersByAccountId(principal.getName());
    }

}
