package com.example.ordermanagement.exceptionHandler;

public class InvalidActionDeleteOrder extends RuntimeException {
    public InvalidActionDeleteOrder(String message) {
        super(message);
    }
}