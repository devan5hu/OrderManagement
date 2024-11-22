package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * MySQL integration for Order Model
 */
public interface OrderRepository extends JpaRepository<Orders, Long> {
   List<Orders> findByCustomerCustomerId(Long Id);
}
