package com.example.ordermanagement.controller;

import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer) {

        Customer newCustomer = customerService.createCustomer(customer) ;
        return new ResponseEntity<>("Customer created with ID: " + newCustomer.getCustomerId(), HttpStatus.CREATED);
    }
}
