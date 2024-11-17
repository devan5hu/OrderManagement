package com.example.ordermanagement.OrderDTO;

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
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }
    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }
}
