package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.CreateOrderRequest;
import com.mikhailkarpov.orders.dto.OrderDto;
import com.mikhailkarpov.orders.dto.OrderItemDto;
import com.mikhailkarpov.orders.entity.Order;
import com.mikhailkarpov.orders.entity.OrderItem;
import com.mikhailkarpov.orders.entity.OrderStatus;
import com.mikhailkarpov.orders.exception.CreateOrderException;
import com.mikhailkarpov.orders.exception.ResourceNotFoundException;
import com.mikhailkarpov.orders.exception.UpdateOrderException;
import com.mikhailkarpov.orders.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;

    @Override
    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {

        Map<String, Integer> productAmountPerCode = request.getItems().stream().collect(Collectors.toMap(
                CreateOrderRequest.CreateOrderItemRequest::getCode,
                CreateOrderRequest.CreateOrderItemRequest::getAmount)
        );
        List<OrderItemDto> products = getProductsByCode(productAmountPerCode.keySet());

        Order order = new Order(request.getAccountId(), OrderStatus.WAITING_FOR_PAYMENT);

        Set<OrderItem> items = products.stream().map(product -> {
            String code = product.getCode();
            String name = product.getName();
            Integer price = product.getPrice();
            Integer amount = productAmountPerCode.get(code);

            if (amount > product.getAmount()) {
                String message = String.format("The number of item %s exceeds available amount", code);
                throw new CreateOrderException(message);
            }

            return new OrderItem(code, name, price, amount);
        }).collect(Collectors.toSet());

        for (OrderItem item : items) {
            order.addItem(item);
        }

        order = orderRepository.save(order);
        log.info("Creating order: {}", order);
        items.forEach(item -> log.info("Creating item: {}", item));

        return mapOrderFromEntity(order);
    }

    private List<OrderItemDto> getProductsByCode(Collection<String> codes) {

        List<OrderItemDto> products = productServiceClient.getProductsByCodes(new ArrayList<>(codes));
        log.info("Got {} product(s):", products.size());
        products.forEach(product -> log.info("{}", product));

        if (products.size() != codes.size()) {
            List<String> notFoundProductsCodes = new ArrayList<>(codes);

            notFoundProductsCodes.removeAll(products.stream()
                    .map(OrderItemDto::getCode)
                    .collect(Collectors.toList())
            );

            String message = String.format("Products with codes=%s not found", notFoundProductsCodes);
            throw new ResourceNotFoundException(message);
        }

        return products;
    }

    @Override
    @Transactional
    public OrderDto updateStatus(UUID id, OrderStatus update) {
        Order order = getById(id);

        if (order.getStatus().equals(OrderStatus.DELIVERED)) {
            String message = String.format("Order with id=%s already delivered", id);
            throw new UpdateOrderException(message);
        }

        order.setStatus(update);
        log.info("Updating order: {}", order);

        return mapOrderFromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto findOrderById(UUID id) {
        Order order = getById(id);
        log.info("Found order: {}", order);

        return mapOrderFromEntity(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findOrdersByAccountId(String accountId) {
        List<OrderDto> orders = orderRepository.findAllByAccountId(accountId).stream()
                .map(this::mapOrderFromEntity)
                .collect(Collectors.toList());

        log.info("Found {} order(s) with accountId={}", orders.size(), accountId);
        return orders;
    }

    private Order getById(UUID id) {
        Optional<Order> found = orderRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("Order with id=%s not found", id);
            throw new ResourceNotFoundException(message);
        }

        return found.get();
    }

    private OrderDto mapOrderFromEntity(Order order) {

        return OrderDto.builder()
                .id(order.getId())
                .accountId(order.getAccountId())
                .status(order.getStatus())
                .items(order
                        .getItems()
                        .stream()
                        .map(this::mapItemFromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderItemDto mapItemFromEntity(OrderItem item) {
        return OrderItemDto.builder()
                .code(item.getCode())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .build();
    }
}
