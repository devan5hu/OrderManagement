package com.example.ordermanagement.repository;

import com.example.ordermanagement.models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Orders, Long> {

   List<Orders> findByCustomerCustomerId(Long Id);
}
