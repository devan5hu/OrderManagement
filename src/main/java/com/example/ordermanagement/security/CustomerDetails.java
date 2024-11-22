package com.example.ordermanagement.security;

import com.example.ordermanagement.models.Customer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class CustomerDetails extends User {
    private Long customerId;

    /**
     * set CustomerId with respect to username and password
     */
    public CustomerDetails(Customer customer) {
        super(customer.getUsername(), customer.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER")));
        this.customerId = customer.getCustomerId();
    }

    /**
     * get CustomerId
     */
    public Long getCustomerId() {
        return customerId;
    }
}
