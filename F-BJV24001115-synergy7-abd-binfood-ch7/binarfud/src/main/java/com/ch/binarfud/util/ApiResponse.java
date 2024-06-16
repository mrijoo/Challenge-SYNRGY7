package com.ch.binarfud.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    private ApiResponse() {
    }

    private static ResponseEntity<Object> buildResponse(HttpStatus status, String message, String result, Object data) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", result);
        response.put("message", message);
        if (data != null) {
            response.put("data", data);
        }
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<Object> success(HttpStatus status, String message) {
        return buildResponse(status, message, "success", null);
    }

    public static ResponseEntity<Object> success(HttpStatus status, String message, Object data) {
        return buildResponse(status, message, "success", data);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message) {
        return buildResponse(status, message, "error", null);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message, Object details) {
        return buildResponse(status, message, "error", details);
    }
}
