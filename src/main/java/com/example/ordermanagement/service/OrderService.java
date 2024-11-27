package com.example.ordermanagement.service;

import com.example.ordermanagement.DTO.OrderItemRequest;
import com.example.ordermanagement.DTO.OrderRequest;
import com.example.ordermanagement.config.AsyncConfig;
import com.example.ordermanagement.exceptionHandler.*;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.models.OrderItems;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderItemRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.ordermanagement.constants.OrderStatusCodes.*;

@Service
public class OrderService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private AsyncConfig asyncConfig;
    /**
     Create New Order
     */
    @Async("asyncExecutor")
    public CompletableFuture<Orders> createOrder(OrderRequest orderRequest, String username) {
        try {
            Customer authenticatedCustomer = customerRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomerNotFoundException("Authenticated customer not found"));

            validateOrderRequest(orderRequest);

            Orders order = new Orders();
            order.setCustomer(authenticatedCustomer);
            order.setStatus("Processing"); // Change to Shipping or Delivered for testing.
            order.setTimestamp(orderRequest.getTimestamp());
            order.setTotalAmount(orderRequest.getTotalAmount());

            List<OrderItems> orderItems = orderRequest.getOrderItems().stream()
                    .map(itemRequest -> new OrderItems(order, itemRequest.getProductId(),
                            itemRequest.getQuantity(), itemRequest.getPrice()))
                    .collect(Collectors.toList());
            order.setOrderItems(orderItems);

            Orders savedOrder = orderRepository.save(order);

            return CompletableFuture.completedFuture(savedOrder);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Data integrity violation: " + e.getMessage());
            throw new InvalidOrder("Unable to save order. Please check the provided data.");
        }
    }

    /**
     * fetch all Orders By Customer Username
     */
    @Async
    public CompletableFuture<List<Orders>> getOrdersByCustomerUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));

        return CompletableFuture.completedFuture(orderRepository.findByCustomerCustomerId(customer.getCustomerId()));
    }

    /**
     * Fetch a Particular Order's status by its ID
     */
    @Async
    public CompletableFuture<String> getOrderStatusForUser(Long orderId, String username) {

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        return CompletableFuture.completedFuture(order.getStatus());
    }

    /**
     * Get Order By ID
     */
    @Async
    public CompletableFuture<Orders> getOrderById(Long orderId, String username) {

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        return CompletableFuture.completedFuture(order);
    }

    /**
     * Update the Order (OrderItems, amount, quantity and so on)
     */
    public CompletableFuture<Orders> updateOrder(Long orderId, OrderRequest orderRequest, String username) {

        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidOrder("Order with ID " + orderId + " not found"));

        if (!existingOrder.getCustomer().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to update this order.");
        }

        if (ORDER_SHIPPED.equals(existingOrder.getStatus()) || ORDER_DELIVERED.equals(existingOrder.getStatus())) {
            throw new InvalidActionUpdateOrder("Order has already been shipped and cannot be modified.");
        }

        validateOrderRequest(orderRequest);
        updateOrderDetails(existingOrder, orderRequest);

        return CompletableFuture.completedFuture(orderRepository.save(existingOrder));
    }

    /**
     * cancel and Delete Order (Better approach would have been to archive them and keep them stored for later use)
     */
    public CompletableFuture<Boolean> cancelAndDeleteOrder(Long orderId, String username) {
        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));
        if (!existingOrder.getCustomer().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to cancel and delete this order.");
        }

        if (ORDER_SHIPPED.equals(existingOrder.getStatus()) || ORDER_DELIVERED.equals(existingOrder.getStatus())) {
            throw new InvalidActionDeleteOrder("Order cannot be cancelled as it has already been processed or shipped.");
        }

        List<OrderItems> orderItems = existingOrder.getOrderItems();

        orderItemRepository.deleteAll(orderItems);
        orderRepository.delete(existingOrder);
        return CompletableFuture.completedFuture(true);
    }


    /**
     Validate The order Request
     */
    private void validateOrderRequest(OrderRequest orderRequest) {
        if (orderRequest.getOrderItems() == null || orderRequest.getOrderItems().isEmpty()) {
            throw new InvalidOrder("Order items cannot be empty");
        }

        if (orderRequest.getTotalAmount() == null || orderRequest.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }

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
    }

    /**
     update Order Details (if there is a change in order_items)
     */
    private void updateOrderDetails(Orders existingOrder, OrderRequest orderRequest) {
        Map<Long, OrderItemRequest> requestItemsMap = orderRequest.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemRequest::getProductId, Function.identity()));

        List<OrderItems> existingItems = getOrderItems(existingOrder, requestItemsMap);

        for (OrderItemRequest newItemRequest : requestItemsMap.values()) {
            OrderItems newItem = new OrderItems(existingOrder, newItemRequest.getProductId(),
                    newItemRequest.getQuantity(), newItemRequest.getPrice());
            existingItems.add(newItem);
        }

        existingOrder.setTotalAmount(orderRequest.getTotalAmount());
        existingOrder.setTimestamp(orderRequest.getTimestamp());
    }

    /**
     fetch OrderItems from the request
     */
    private static List<OrderItems> getOrderItems(Orders existingOrder, Map<Long, OrderItemRequest> requestItemsMap) {
        List<OrderItems> existingItems = existingOrder.getOrderItems();

        existingItems.removeIf(existingItem -> {
            OrderItemRequest requestItem = requestItemsMap.get(existingItem.getProductId());
            if (requestItem != null) {
                existingItem.setQuantity(requestItem.getQuantity());
                existingItem.setPrice(requestItem.getPrice());
                requestItemsMap.remove(existingItem.getProductId());
                return false;
            }
            return true;
        });

        return existingItems;
    }

}
