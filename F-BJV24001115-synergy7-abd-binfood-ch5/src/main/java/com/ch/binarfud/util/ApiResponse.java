package com.ch.binarfud.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    private ApiResponse() {}

    public static ResponseEntity<Object> success(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);

        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> success(HttpStatus status, String message, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);

        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);

        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message, Object details) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("details", details);

        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message, List<String> details) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("details", details);
    
        return ResponseEntity.status(status).body(response);
    }
}
