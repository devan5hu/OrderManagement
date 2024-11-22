package com.example.ordermanagement.DTO;

public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private String status;
    private String message;

    // Default constructor
    public LoginResponse() {}

    // Constructor for success response with token
    public LoginResponse(String token, String status, String tokenType) {
        this.token = token;
        this.status = status;
        this.tokenType = tokenType;
    }

    // Constructor for error response
    public LoginResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}