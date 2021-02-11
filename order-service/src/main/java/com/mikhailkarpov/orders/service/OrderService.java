package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.CreateOrderRequest;
import com.mikhailkarpov.orders.dto.OrderDto;
import com.mikhailkarpov.orders.entity.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderDto createOrder(String customerId, CreateOrderRequest request);

    OrderDto updateStatus(UUID id, OrderStatus update);

    OrderDto findOrderById(UUID id);

    List<OrderDto> findOrdersByCustomerId(String customerId);
}
