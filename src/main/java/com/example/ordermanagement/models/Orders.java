package com.example.ordermanagement.models;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    private String status;
    private Timestamp timestamp;
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItems> orderItems;  // List of order items

    // Constructors
    public Orders() {}

    public Orders(Customer customer, String status, Timestamp timestamp, BigDecimal totalAmount) {
        this.customer = customer;
        this.status = status;
        this.timestamp = timestamp;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public Customer getCustomer() { return customer; }


    public void setCustomer(Customer customer) { this.customer = customer; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public void setOrderItems(List<OrderItems> orderItems) { this.orderItems = orderItems; }
}
