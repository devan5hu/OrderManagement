package com.example.ordermanagement.exceptionHandler;

public class InvalidOrder extends RuntimeException {
    public InvalidOrder(String message) {
        super(message);
    }
}
