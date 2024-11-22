package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * MySQL integration for Customer Model
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional <Customer> findByUsername(String username);
    Optional <Customer> findByPhone(String email);
}
