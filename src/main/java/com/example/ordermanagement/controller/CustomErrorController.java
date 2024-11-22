package com.example.ordermanagement.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static com.example.ordermanagement.constants.ErrorCodes.INVALID_REQUEST_OR_ROUTE;

@RestController
public class CustomErrorController implements ErrorController {

    /**
     * Error Page for invalid routes
     */

    @PostMapping("/**")
    public ResponseEntity<Map<String, String>> postErrorHandling() {
        HashMap<String , String> response = new HashMap<>();
        response.put("status", INVALID_REQUEST_OR_ROUTE );
        response.put("message", "Invalid Request or Route please check again." );
        return  new ResponseEntity<>( response , HttpStatus.BAD_REQUEST);
    }
    @DeleteMapping("/**")
    public ResponseEntity<Map<String, String>> deleteErrorHandling() {
        HashMap<String , String> response = new HashMap<>();
        response.put("status", INVALID_REQUEST_OR_ROUTE );
        response.put("message", "Invalid Request or Route please check again." );
        return  new ResponseEntity<>( response , HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/**")
    public ResponseEntity<Map<String, String>> putErrorHandling() {
        HashMap<String , String> response = new HashMap<>();
        response.put("status", INVALID_REQUEST_OR_ROUTE );
        response.put("message", "Invalid Request or Route please check again." );
        return  new ResponseEntity<>( response , HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/error")
    public CompletableFuture<String> handleError() {
        return CompletableFuture.completedFuture("/error/404");
    }
}
