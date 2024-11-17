package com.example.ordermanagement.controller;

import java.util.List;

import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.OrderDTO.OrderResponse;
import com.example.ordermanagement.models.Orders;
import com.example.ordermanagement.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order API
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        Orders order = orderService.createOrder(orderRequest);

        // Create a response object
        OrderResponse orderResponse = new OrderResponse("Order Placed", order.getOrderId());

        // Return a 200 status with the custom message and orderId
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    // Get Order by ID API
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get All Orders API
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Delete Order API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>("Order with ID " + id + " has been deleted.", HttpStatus.OK);
    }

    @GetMapping("/my-orders/{customerId}")
    public ResponseEntity<List<Orders>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<Orders> orders = orderService.getOrdersByCustomerId(customerId);
        return orders.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(orders);
    }
}
