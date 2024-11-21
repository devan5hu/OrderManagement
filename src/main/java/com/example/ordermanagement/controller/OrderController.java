package com.example.ordermanagement.controller;

import java.util.HashMap;
import java.util.List;

import static com.example.ordermanagement.constants.OrderStatusCodes.ORDER_PLACED;

import com.example.ordermanagement.OrderDTO.OrderRequest;
import com.example.ordermanagement.OrderDTO.OrderStatusResponse;
import com.example.ordermanagement.models.Orders;
import com.example.ordermanagement.service.CustomerService;
import com.example.ordermanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private CustomerService customerService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Create Order API
    @PostMapping
    public ResponseEntity<OrderStatusResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        String username = getAuthenticatedUsername();   // Get username

        Orders order = orderService.createOrder(orderRequest, username);
        OrderStatusResponse orderResponse = new OrderStatusResponse(ORDER_PLACED, order.getOrderId());

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @GetMapping("/my-orders")
    public List<Orders> getCustomerOrders() {
        String username = getAuthenticatedUsername(); // Get username

        return orderService.getOrdersByCustomerUsername(username);
    }

    @GetMapping("/status/{order_id}")
    public ResponseEntity<Map<String , String>> getOrderStatusById(@PathVariable Long order_id) {
        String username = getAuthenticatedUsername();

        String status = orderService.getOrderStatusForUser(order_id, username);

        Map< String , String > response = new HashMap<>();
        response.put("OrderStatus" , status);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{order_id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable Long order_id, @RequestBody OrderRequest orderRequest) {
        String username = getAuthenticatedUsername();

        Orders updatedOrder = orderService.updateOrder(order_id, orderRequest, username);

        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<Map <String , String>> cancelAndDeleteOrder(@PathVariable Long orderId) {
        String username = getAuthenticatedUsername();

        orderService.cancelAndDeleteOrder(orderId, username);

        Map<String , String> response = new HashMap<>();
        response.put("Status" , "Success");

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String getAuthenticatedUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

}
