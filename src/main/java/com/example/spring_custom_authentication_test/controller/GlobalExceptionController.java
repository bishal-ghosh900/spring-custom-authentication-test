package com.example.spring_custom_authentication_test.controller;

import com.example.spring_custom_authentication_test.UserException;
import com.example.spring_custom_authentication_test.entity.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(value = {UserException.class, UsernameNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleUserExceptions(Exception ex){
        ErrorResponse errorResponse = new ErrorResponse();

        if(ex instanceof UserException) {
            errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        } else {
            errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        }

        errorResponse.setMessage(ex.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return ResponseEntity
                .status(HttpStatus.valueOf(errorResponse.getStatus()))
                .body(errorResponse);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> handleAllException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setMessage(ex.getMessage());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
