package com.example.ordermanagement.OrderDTO;

public class OrderStatusResponse {

    private final String status;
    private final Long orderId;

    // Constructors
    public OrderStatusResponse(String status, Long orderId) {
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
