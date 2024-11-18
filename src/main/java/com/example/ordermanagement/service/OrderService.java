package com.example.ordermanagement.service;

import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.models.OrderItems;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
        // Find customer by ID
        Customer customer = null;
        try{
            Optional<Customer> customers = customerRepository.findById(orderRequest.getCustomerId());
            if(customers.isPresent()){
               customer = customers.get();
            }
            // Create Order entity
            Orders order = new Orders();
            order.setCustomer(customer);
            order.setStatus(orderRequest.getStatus());
            order.setTimestamp(orderRequest.getTimestamp());
            order.setTotalAmount(orderRequest.getTotalAmount());

            // Convert OrderItemRequest to OrderItem entities
            List<OrderItems> orderItems = orderRequest.getOrderItems().stream()
                    .map(itemRequest -> new OrderItems(order, itemRequest.getProductId(),
                            itemRequest.getQuantity(), itemRequest.getPrice()))
                    .collect(Collectors.toList());

            // Set the items to the order
            order.setOrderItems(orderItems);

            // Save and return the order
            return orderRepository.save(order);

        } catch (DataIntegrityViolationException e) {

            throw new IllegalArgumentException("Customer Not Found");
        }
    }

    public Optional<Orders> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrdersByCustomerId(Long customerId) {return orderRepository.findByCustomerCustomerId(customerId);}

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
