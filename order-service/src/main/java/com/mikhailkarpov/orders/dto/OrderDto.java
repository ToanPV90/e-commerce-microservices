package com.mikhailkarpov.orders.dto;

import com.mikhailkarpov.orders.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {

    private UUID id;

    private String accountId;

    private List<OrderItemDto> items;

    private OrderStatus status;
}
