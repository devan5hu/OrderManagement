package com.example.ordermanagement.service;

import com.example.ordermanagement.OrderDTO.OrderItemRequest;
import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.exceptionHandler.CustomerNotFoundException;
import com.example.ordermanagement.exceptionHandler.InvalidOrder;
import com.example.ordermanagement.exceptionHandler.OrderNotFoundException;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.models.OrderItems;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Orders createOrder(OrderRequest orderRequest) {

        if (orderRequest.getCustomerId() == null || orderRequest.getCustomerId() <= 0) {
            throw new CustomerNotFoundException("Invalid Customer ID");
        }
        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new InvalidOrder("Order items cannot be empty");
        }
        if (orderRequest.getTotalAmount() == null || orderRequest.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }

        // Validate each order item
        for (OrderItemRequest item : orderRequest.getOrderItems()) {
            if (item.getProductId() == null || item.getProductId() <= 0) {
                throw new IllegalArgumentException("Invalid Product ID in order item");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero");
            }
            if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero");
            }
        }
        // Find customer by ID
        Customer customer = customerRepository.findById(orderRequest.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + orderRequest.getCustomerId() + " not found."));

        // Create Order entity
        Orders order = new Orders();
        order.setCustomer(customer);
        order.setStatus("Order Placed");
        order.setTimestamp(orderRequest.getTimestamp());
        order.setTotalAmount(orderRequest.getTotalAmount());

        // Convert OrderItemRequest to OrderItem entities
        List<OrderItems> orderItems = orderRequest.getOrderItems().stream()
                .map(itemRequest -> new OrderItems(order, itemRequest.getProductId(),
                        itemRequest.getQuantity(), itemRequest.getPrice()))
                .collect(Collectors.toList());

        // Set the items to the order
        order.setOrderItems(orderItems);

        try {
            // Save and return the order
            return orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Unable to save order. Please check the provided data.");
        }
    }


    public Optional<Orders> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public String getOrderStatus(Long orderId) {
        return orderRepository.findById(orderId)
                .map(Orders::getStatus).orElseThrow(() ->
                        new OrderNotFoundException("Order with ID " + orderId + " not found."));
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrdersByCustomerId(Long customerId) {return orderRepository.findByCustomerCustomerId(customerId);}

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
