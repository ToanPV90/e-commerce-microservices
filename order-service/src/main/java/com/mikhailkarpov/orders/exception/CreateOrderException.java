package com.mikhailkarpov.orders.exception;

public class CreateOrderException extends RuntimeException {

    public CreateOrderException(String message) {
        super(message);
    }

    public CreateOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateOrderException(Throwable cause) {
        super(cause);
    }
}
