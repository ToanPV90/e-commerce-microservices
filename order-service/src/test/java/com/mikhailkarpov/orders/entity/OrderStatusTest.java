package com.mikhailkarpov.orders.entity;

import org.junit.jupiter.api.Test;

import static com.mikhailkarpov.orders.entity.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStatusTest {

    @Test
    void waitingForPayment_isPossibleToUpdate() {

        assertTrue(AWAITING_FOR_PAYMENT.isPossibleToUpdateTo(HAS_BEEN_PAYED));
        assertTrue(HAS_BEEN_PAYED.isPossibleToUpdateTo(AWAITING_FOR_DISPATCH));
        assertTrue(AWAITING_FOR_DISPATCH.isPossibleToUpdateTo(SENT));
        assertTrue(SENT.isPossibleToUpdateTo(DELIVERED));

        assertFalse(HAS_BEEN_PAYED.isPossibleToUpdateTo(AWAITING_FOR_PAYMENT));
        assertFalse(DELIVERED.isPossibleToUpdateTo(AWAITING_FOR_PAYMENT));
    }
}