package com.mikhailkarpov.customers.config;

import com.mikhailkarpov.customers.dto.ApiErrorResponse;
import com.mikhailkarpov.customers.exception.BadRequestException;
import com.mikhailkarpov.customers.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.customers.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), NOT_FOUND, request);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    protected ResponseEntity<Object> handleResourceAlreadyExists(ResourceAlreadyExistsException ex, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequest(BadRequestException ex, WebRequest request) {
        ApiErrorResponse response = new ApiErrorResponse(ex);
        return handleExceptionInternal(ex, response, new HttpHeaders(), BAD_REQUEST, request);
    }

}
