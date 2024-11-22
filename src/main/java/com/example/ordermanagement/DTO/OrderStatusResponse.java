package com.example.ordermanagement.DTO;

public class OrderStatusResponse {
    private String status;
    private Long orderId;

    // Constructor
    public OrderStatusResponse(String status, Long orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}

