package com.example.ordermanagement.controller;

import java.util.List;

import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.OrderDTO.OrderResponse;
import com.example.ordermanagement.models.Orders;
import com.example.ordermanagement.service.CustomerService;
import com.example.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private CustomerService customerService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Get All Orders API
    @GetMapping
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // Create Order API
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest orderRequest) {
        Orders order = orderService.createOrder(orderRequest);
        OrderResponse orderResponse = new OrderResponse("Order Placed", order.getOrderId());
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    // Get Order by ID API
    @GetMapping("/{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long id) {
        Optional<Orders> order = orderService.getOrderById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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

    @GetMapping("/status/{order_id}")
    public ResponseEntity<String> getOrderStatusById(@PathVariable Long order_id) {
        String status = orderService.getOrderStatus(order_id);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }
}
