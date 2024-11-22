package com.example.ordermanagement.controller;

import com.example.ordermanagement.DTO.LoginRequest;
import com.example.ordermanagement.DTO.LoginResponse;
import com.example.ordermanagement.auth.JwtTokenProvider;
import com.example.ordermanagement.exceptionHandler.DuplicatePhoneException;
import com.example.ordermanagement.exceptionHandler.DuplicateUsernameException;
import com.example.ordermanagement.models.Customer;
import com.example.ordermanagement.security.CustomerDetails;
import com.example.ordermanagement.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static com.example.ordermanagement.constants.ErrorCodes.ACTION_FAILED;
import static com.example.ordermanagement.constants.ErrorCodes.ACTION_SUCCESS;
import static com.example.ordermanagement.constants.GeneralCodes.BEARER_TOKEN;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * Register endpoint
     */
    @PostMapping("/api/register")
    @Async
    public CompletableFuture<ResponseEntity<LoginResponse>> register(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.createCustomer(customer);

            CustomerDetails customerDetails = new CustomerDetails(createdCustomer);

            String token = jwtTokenProvider.generateToken(customerDetails);

            LoginResponse response = new LoginResponse(token, ACTION_SUCCESS, BEARER_TOKEN);
            return CompletableFuture.completedFuture(ResponseEntity.ok(response));
        } catch (DuplicateUsernameException | DuplicatePhoneException e) {
            LoginResponse errorResponse = new LoginResponse(ACTION_FAILED, e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
        }
    }

    /**
     * Login endpoint
     */
    @PostMapping("/api/login")
    @Async
    public CompletableFuture<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = customerService.authenticate(loginRequest);
            return CompletableFuture.completedFuture(ResponseEntity.ok(response));
        } catch (BadCredentialsException e) {
            LoginResponse errorResponse = new LoginResponse(ACTION_FAILED, e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse));
        }
    }

}
