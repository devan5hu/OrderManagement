package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItems, Long> {

}
