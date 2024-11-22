package com.example.ordermanagement.service;

import com.example.ordermanagement.DTO.LoginRequest;
import com.example.ordermanagement.DTO.LoginResponse;
import com.example.ordermanagement.exceptionHandler.DuplicatePhoneException;
import com.example.ordermanagement.exceptionHandler.DuplicateUsernameException;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.security.CustomerDetails;
import com.example.ordermanagement.auth.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import static com.example.ordermanagement.constants.ErrorCodes.ACTION_SUCCESS;
import static com.example.ordermanagement.constants.GeneralCodes.BEARER_TOKEN;

@Service
public class CustomerService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Load user by username for authentication.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with username: " + username));

        return new CustomerDetails(customer);
    }

    /**
     * Create a new customer account.
     */
    public Customer createCustomer(Customer customer) {
        if (customerRepository.findByUsername(customer.getUsername()).isPresent()) {
            throw new DuplicateUsernameException("Username already in use. Please choose a different one.");
        }

        if (customerRepository.findByPhone(customer.getPhone()).isPresent()) {
            throw new DuplicatePhoneException("Phone number already in use. Please use another or login with the same again.");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    /**
     * Authenticate a user and return a JWT token.
     */
    public LoginResponse authenticate(LoginRequest loginRequest) {
        Customer customer = customerRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Customer not found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        CustomerDetails customerDetails = new CustomerDetails(customer);
        String token = jwtTokenProvider.generateToken(customerDetails);

        return new LoginResponse(token, ACTION_SUCCESS, BEARER_TOKEN);
    }
}
