package com.Alencar.demo.infrastructure;

import com.Alencar.demo.infrastructure.exceptions.BusinessException;
import com.Alencar.demo.infrastructure.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFound(ResourceNotFoundException e, HttpServletRequest request){
        return buildResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado", e.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String,Object>> businessError(BusinessException e, HttpServletRequest request){
        return buildResponse(HttpStatus.BAD_REQUEST, "Erro de regra de negócio", e.getMessage(),request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message, String path) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", Instant.now());
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("path", path);
        return ResponseEntity.status(status).body(body);
    }

}
