package com.example.ordermanagement.OrderDTO;

public class OrderResponse {

    private final String status;
    private final Long orderId;

    // Constructors
    public OrderResponse(String status, Long orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }
    public Long getOrderId() {
        return orderId;
    }

}
