package com.example.ordermanagement.exceptionHandler;

public class InvalidActionUpdateOrder extends RuntimeException {
    public InvalidActionUpdateOrder(String message) {
        super(message);
    }
}