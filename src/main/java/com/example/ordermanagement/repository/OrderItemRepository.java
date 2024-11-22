package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * MySQL integration for OrderItem Model
 */
public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

}
