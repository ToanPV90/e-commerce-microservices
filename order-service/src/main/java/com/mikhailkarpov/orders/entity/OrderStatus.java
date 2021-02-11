package com.mikhailkarpov.orders.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderStatus {

    AWAITING_FOR_PAYMENT("Awaiting for payment"),
    HAS_BEEN_PAYED("Has been payed"),
    AWAITING_FOR_DISPATCH("Awaiting for shipping"),
    SENT("Sent"),
    DELIVERED("Delivered");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

    public boolean isPossibleToUpdateTo(OrderStatus update) {
        return this.ordinal() < update.ordinal();
    }
}
