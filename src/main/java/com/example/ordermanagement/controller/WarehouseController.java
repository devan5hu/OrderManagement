package com.example.ordermanagement.controller;

import com.example.ordermanagement.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.ordermanagement.constants.ErrorCodes.ACTION_SUCCESS;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/warehouse")
public class WarehouseController {

    @Autowired
    private WarehouseService warehouseService;

    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> requestBody) {
        String status = requestBody.get("status");
        warehouseService.updateOrderStatus(orderId, status);
        Map < String , String > mp = new HashMap<String, String>();
        mp.put("orderStatus", status);
        mp.put("status", ACTION_SUCCESS );

        return new ResponseEntity<>(mp , HttpStatus.OK);
    }
}
