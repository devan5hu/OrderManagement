package com.example.ordermanagement.OrderDTO;

import java.math.BigDecimal;

public class OrderItemRequest {

    private Long productId;
    private Integer quantity;
    private BigDecimal price;

    // Getters and Setters

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
