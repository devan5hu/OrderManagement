package com.example.ordermanagement.exceptionHandler;

import com.example.ordermanagement.DTO.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.example.ordermanagement.constants.ErrorCodes.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(DuplicatePhoneException.class)
    public ResponseEntity<LoginResponse> handleDuplicatePhoneException(DuplicatePhoneException ex) {
        LoginResponse errorResponse = new LoginResponse(ACTION_FAILED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        logger.error("Customer not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFoundException(OrderNotFoundException ex) {
        logger.error("Order not found: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrder.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrder(InvalidOrder ex) {
        logger.error("Invalid Order: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), INVALID_ORDER, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.error("Illegal argument exception: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), GENERAL_ERROR, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<LoginResponse> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        LoginResponse errorResponse = new LoginResponse(ACTION_FAILED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedError(AccessDeniedException ex) {
        logger.error("No permission error: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), ACCESS_DENIED_ERROR , HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        logger.error("Invalid Credentials error: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), INVALID_CREDENTIALS , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidActionUpdateOrder.class)
    public ResponseEntity<ErrorResponse> handleInvalidActionUpdateOrder(InvalidActionUpdateOrder ex) {
        logger.error("Invalid Action Order cannot be modified: {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), ORDER_SHIPPED_ERROR , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
        logger.error("Invalid Token error : {}", ex.getMessage());
        return buildErrorResponse(ex.getMessage(), INVALID_TOKEN , HttpStatus.BAD_REQUEST);
    }

    // Order has already been shipped and cannot be modified.
    @ExceptionHandler(InvalidActionDeleteOrder.class)
    public ResponseEntity<ErrorResponse> handleInvalidActionDeleteOrder(InvalidActionDeleteOrder ex) {
        logger.error("Order Shipped cannot cancle: {}", ex.getMessage(), ex);
        return buildErrorResponse("Order has already been shipped and cannot be modified.", ORDER_SHIPPED_ERROR, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        logger.error("Unexpected exception: {}", ex.getMessage(), ex);
        return buildErrorResponse("An unexpected error occurred. Please try again.", INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String message, String errorCode, HttpStatus status) {
        ErrorResponse errorResponse = new ErrorResponse(message, errorCode);
        return new ResponseEntity<>(errorResponse, status);
    }
}
