package com.mikhailkarpov.orders.repository;

import com.mikhailkarpov.orders.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends CrudRepository<Order, UUID> {

    List<Order> findAllByCustomerId(String customerId);
}
