package com.example.ordermanagement.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice()
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        HashMap<String, String> errorResponse = new HashMap<>();

        if(ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("phone") ) {
            errorResponse.put("message", "Phone number already in use. Please use another or login with the same again.");
            errorResponse.put("errorCode" , "DUPLICATE_PHONE");
        }
        else if(ex.getMessage().contains("Customer Not Found")){
            errorResponse.put("message", "Customer not found please send a valid customer_id");
            errorResponse.put("errorCode" , "CUSTOMER_NOT_FOUND");
        }
        else{
            errorResponse.put("message", ex.getMessage());
            errorResponse.put("errorCode" , "GENERAL ERROR");
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
