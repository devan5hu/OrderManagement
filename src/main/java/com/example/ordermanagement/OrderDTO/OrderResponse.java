package com.example.ordermanagement.OrderDTO;

public class OrderResponse {

    private String status;
    private Long orderId;

    // Constructors
    public OrderResponse(String status, Long orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    // Getters and Setters
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
}
