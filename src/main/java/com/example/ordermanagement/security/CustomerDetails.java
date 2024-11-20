package com.example.ordermanagement.security;

import com.example.ordermanagement.models.Customer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;
import java.util.Optional;

public class CustomerDetails extends User {

    private Long customerId;

    public CustomerDetails(Customer customer) {
        super(customer.getUsername(), customer.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        this.customerId = customer.getCustomerId();
    }

    public Long getCustomerId() {
        return customerId;
    }
}
