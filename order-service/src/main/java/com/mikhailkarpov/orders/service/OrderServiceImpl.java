package com.mikhailkarpov.orders.service;

import com.mikhailkarpov.orders.dto.AddressDto;
import com.mikhailkarpov.orders.dto.CreateOrderRequest;
import com.mikhailkarpov.orders.dto.OrderDto;
import com.mikhailkarpov.orders.dto.ProductDto;
import com.mikhailkarpov.orders.entity.Address;
import com.mikhailkarpov.orders.entity.Order;
import com.mikhailkarpov.orders.entity.OrderItem;
import com.mikhailkarpov.orders.entity.OrderStatus;
import com.mikhailkarpov.orders.exception.ResourceNotFoundException;
import com.mikhailkarpov.orders.exception.UpdateOrderException;
import com.mikhailkarpov.orders.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.mikhailkarpov.orders.entity.OrderStatus.AWAITING_FOR_PAYMENT;

@Slf4j
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public OrderDto createOrder(String customerId, CreateOrderRequest request) {

        Map<String, Integer> productAmountPerCode = request.getItems().stream().collect(Collectors.toMap(
                CreateOrderRequest.CreateOrderItemRequest::getCode,
                CreateOrderRequest.CreateOrderItemRequest::getAmount)
        );

        Set<OrderItem> orderItems = getProductsByCode(productAmountPerCode.keySet()).stream().map(product -> {
            String code = product.getCode();
            String name = product.getName();
            Integer price = product.getPrice();
            Integer amount = productAmountPerCode.get(code);

            return new OrderItem(code, name, price, amount);
        }).collect(Collectors.toSet());

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setAddress(createAddress(request));
        order.setItems(orderItems);
        order.setStatus(AWAITING_FOR_PAYMENT);
        order = orderRepository.save(order);

        log.info("Creating order: {}", order);;
        orderItems.forEach(orderItem -> log.info("\t{}", orderItem));

        return mapOrderFromEntity(order);
    }

    private Address createAddress(CreateOrderRequest request) {

        AddressDto address = request.getAddress();
        return new Address(address.getZip(), address.getCountry(), address.getCity(), address.getStreet());
    }

    private List<ProductDto> getProductsByCode(Collection<String> codes) {

        List<ProductDto> products = productService.getProductsByCodes(new ArrayList<>(codes));
        log.info("Got {} product(s):", products.size());
        products.forEach(product -> log.info("{}", product));

        if (products.size() != codes.size()) {
            List<String> notFoundProductsCodes = new ArrayList<>(codes);

            notFoundProductsCodes.removeAll(products.stream()
                    .map(ProductDto::getCode)
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

        OrderStatus status = order.getStatus();
        if (!status.isPossibleToUpdateTo(update)) {
            String message = String.format("Status '{}' can't be updated to '{}'", status.getTitle(), update.getTitle());
            log.warn(message);
            throw new UpdateOrderException(message);
        }

        order.setStatus(update);
        log.info("Updating order status from '{}' to '{}'", status.getTitle(), update.getTitle());

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
    public List<OrderDto> findOrdersByCustomerId(String customerId) {

        List<OrderDto> orders = orderRepository.findAllByCustomerId(customerId).stream()
                .map(this::mapOrderFromEntity)
                .collect(Collectors.toList());

        log.info("Found {} order(s) with accountId={}", orders.size(), customerId);
        return orders;
    }

    private Order getById(UUID id) {

        Optional<Order> found = orderRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("Order with id=%s not found", id);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        return found.get();
    }

    private OrderDto mapOrderFromEntity(Order order) {

        return OrderDto.builder()
                .id(order.getId())
                .accountId(order.getCustomerId())
                .address(mapAddressFromEntity(order.getAddress()))
                .status(order.getStatus())
                .items(order.getItems().stream()
                        .map(this::mapItemFromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    private ProductDto mapItemFromEntity(OrderItem item) {

        return ProductDto.builder()
                .code(item.getCode())
                .name(item.getName())
                .price(item.getPrice())
                .amount(item.getAmount())
                .build();
    }

    private AddressDto mapAddressFromEntity(Address address) {

        return AddressDto.builder()
                .zip(address.getZip())
                .country(address.getCountry())
                .city(address.getCity())
                .street(address.getStreet())
                .build();
    }
}
