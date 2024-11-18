package com.example.ordermanagement.service;

import com.example.ordermanagement.exceptionHandler.GlobalExceptionHandler;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() { return customerRepository.findAll(); }

    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer createCustomer(Customer customer) {
        try {
            return customerRepository.save(customer);
        } catch (DataIntegrityViolationException ex) {
            System.out.println("Duplicate Error Message " + ex.getMessage());
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    public void deleteCustomer(Long customerId) {customerRepository.deleteById(customerId);}
}
