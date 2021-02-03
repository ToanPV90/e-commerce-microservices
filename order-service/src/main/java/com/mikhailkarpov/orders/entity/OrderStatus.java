package com.mikhailkarpov.orders.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {

    WAITING_FOR_PAYMENT("Waiting for payment"),
    WAITING_FOR_SHIPPING("Waiting for shipping"),
    WAITING_FOR_DELIVERY("Waiting for delivery"),
    DELIVERED("Delivered");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }
}
