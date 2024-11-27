package com.example.ordermanagement.service;

import com.example.ordermanagement.models.Orders;
import com.example.ordermanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WarehouseService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + orderId + " not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
