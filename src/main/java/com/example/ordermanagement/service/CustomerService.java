package com.example.ordermanagement.service;

import com.example.ordermanagement.exceptionHandler.DuplicatePhoneException;
import com.example.ordermanagement.exceptionHandler.DuplicateUsernameException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.security.CustomerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch the customer by their username
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with username: " + username));

        return new CustomerDetails(customer);
    }

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        // Check if a username already exists
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already in use. Please choose a different one.");
        }

        // Check if a phone number already exists
        if (customerRepository.findByPhone(customer.getPhone()).isPresent()) {
            throw new DuplicatePhoneException("Phone number already in use. Please use another or login with the same again.");
        }

        // Hash the password before saving the customer
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));

        // Save the customer if no duplicates are found
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long customerId) {customerRepository.deleteById(customerId);}
}
