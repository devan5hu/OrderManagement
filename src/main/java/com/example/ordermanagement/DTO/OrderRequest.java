package com.example.ordermanagement.DTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class OrderRequest {

    private Long customerId;
    private String status;
    private Timestamp timestamp;
    private BigDecimal totalAmount;
    private List<OrderItemRequest> orderItems;

    // Getters and Setters
    public OrderRequest() {

    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
}
