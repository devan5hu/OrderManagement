package com.example.ordermanagement.DTO;

import java.math.BigDecimal;

public class OrderItemRequest {

    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    // Getters and Setters

    public BigDecimal getPrice() {
        return price;
    }
    public Long getProductId() {
        return productId;
    }
    public Integer getQuantity() {
        return quantity;
    }

}
