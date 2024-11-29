package com.example.ordermanagement.controller;

import java.util.HashMap;
import java.util.List;

import com.example.ordermanagement.DTO.OrderRequest;
import com.example.ordermanagement.DTO.OrderStatusResponse;
import com.example.ordermanagement.auth.JwtTokenProvider;
import com.example.ordermanagement.exceptionHandler.*;
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
import java.util.concurrent.ExecutionException;



@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {"*"})
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }



    // Create Order API
    @PostMapping("/place")
    public ResponseEntity<OrderStatusResponse> createOrder(@RequestBody OrderRequest orderRequest) {
        String username = getAuthenticatedUsername(); // Get username
        try {
            Orders order = orderService.createOrder(orderRequest, username).get();
            if (order == null) {
                return new ResponseEntity<>(new OrderStatusResponse("ORDER_FAILED", null), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(new OrderStatusResponse("ORDER_PLACED", order.getOrderId()), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(new OrderStatusResponse("ORDER_FAILED", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/my-orders")
    public List<Orders> getCustomerOrders() throws ExecutionException, InterruptedException {
        String username = getAuthenticatedUsername(); // Get username

        return orderService.getOrdersByCustomerUsername(username).get();
    }

    @GetMapping("/status/{order_id}")
    public ResponseEntity<Map<String , String>> getOrderStatusById(@PathVariable Long order_id) throws ExecutionException, InterruptedException {
        try {
            String username = getAuthenticatedUsername();

            String status = orderService.getOrderStatusForUser(order_id, username).get();

            Map<String, String> response = new HashMap<>();
            response.put("OrderStatus", status);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (ExecutionException ex) {
            throw new InvalidOrder("Invalid Order Id");
        }
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable Long order_id) throws ExecutionException, InterruptedException {
        try {
            String username = getAuthenticatedUsername();

            Orders order = orderService.getOrderById(order_id, username).get();

            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (ExecutionException ex) {
            throw new InvalidOrder("Invalid Order Id");
        }
    }

    @PutMapping("/update/{order_id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable Long order_id, @RequestBody OrderRequest orderRequest) throws ExecutionException, InterruptedException {
        try {
            String username = getAuthenticatedUsername();

            Orders updatedOrder = orderService.updateOrder(order_id, orderRequest, username).get();

            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        }
        catch (InvalidOrder | AccessDeniedException ex ) {
            throw new InvalidOrder("Invalid Order Id");
        } catch(InvalidActionUpdateOrder | ExecutionException ex){
            throw new InvalidActionUpdateOrder("Order Already Shipped or Delivered");
        }
    }

    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancelAndDeleteOrder(@PathVariable Long orderId) {
        try{
            String username = getAuthenticatedUsername();

            Boolean status = orderService.cancelAndDeleteOrder(orderId, username).get();

            if(status){
                HashMap<String , String> response = new HashMap<>();
                response.put("OrderStatus", "CANCELLED");
                return new ResponseEntity<>( response, HttpStatus.OK);
            }
        }catch(AccessDeniedException | OrderNotFoundException ex){
            throw new InvalidOrder("Invalid Order Id");
        } catch(ExecutionException ex){
            throw new InvalidActionUpdateOrder("Order Already Shipped or Delivered");
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
