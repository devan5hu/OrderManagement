package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
