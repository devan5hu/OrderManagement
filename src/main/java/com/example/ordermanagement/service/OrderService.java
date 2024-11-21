package com.example.ordermanagement.service;

import com.example.ordermanagement.OrderDTO.OrderItemRequest;
import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.exceptionHandler.*;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.models.OrderItems;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderItemRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.models.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
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

    public Orders createOrder(OrderRequest orderRequest, String username) {
        try {
            Customer authenticatedCustomer = customerRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomerNotFoundException("Authenticated customer not found"));

            validateOrderRequest(orderRequest);

            Orders order = new Orders();
            order.setCustomer(authenticatedCustomer);
            order.setStatus(ORDER_PLACED);
            order.setTimestamp(orderRequest.getTimestamp());
            order.setTotalAmount(orderRequest.getTotalAmount());

            List<OrderItems> orderItems = orderRequest.getOrderItems().stream()
                    .map(itemRequest -> new OrderItems(order, itemRequest.getProductId(),
                            itemRequest.getQuantity(), itemRequest.getPrice()))
                    .collect(Collectors.toList());

            order.setOrderItems(orderItems);

            return orderRepository.save(order);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidOrder("Unable to save order. Please check the provided data.");
        }
    }

    public List<Orders> getOrdersByCustomerUsername(String username) {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with username: " + username));

        return orderRepository.findByCustomerCustomerId(customer.getCustomerId());
    }

    public String getOrderStatusForUser(Long orderId, String username) {

        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with username: " + username));

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (!order.getCustomer().getCustomerId().equals(customer.getCustomerId())) {
            throw new AccessDeniedException("You do not have permission to access this order.");
        }

        return order.getStatus();
    }

    public Orders updateOrder(Long orderId, OrderRequest orderRequest, String username) {

        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + orderId + " not found"));

        if (!existingOrder.getCustomer().getUsername().equals(username)) {
            throw new AccessDeniedException("You are not authorized to update this order.");
        }

        if (ORDER_SHIPPED.equals(existingOrder.getStatus()) || ORDER_DELIVERED.equals(existingOrder.getStatus())) {
            throw new InvalidActionUpdateOrder("Order has already been shipped and cannot be modified.");
        }

        validateOrderRequest(orderRequest);
        updateOrderDetails(existingOrder, orderRequest);

        return orderRepository.save(existingOrder);
    }

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

    public void cancelAndDeleteOrder(Long orderId, String username) {
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
    }
}
