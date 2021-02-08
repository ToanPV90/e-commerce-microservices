package com.mikhailkarpov.customers.config;

import com.mikhailkarpov.customers.dto.ApiErrorResponse;
import com.mikhailkarpov.customers.exception.ConflictException;
import com.mikhailkarpov.customers.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        HttpStatus status = NOT_FOUND;
        ApiErrorResponse errorResponse = new ApiErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(ConflictException ex, WebRequest request) {

        HttpStatus status = CONFLICT;
        ApiErrorResponse errorResponse = new ApiErrorResponse(ex, status);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error(ex.getMessage());

        String message = "Unexpected error occurred. Please, try again later";
        HttpStatus status = INTERNAL_SERVER_ERROR;
        ApiErrorResponse errorResponse = new ApiErrorResponse(message, status);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        String message =
                String.format("Handling %s: %s. Sending %s", ex.getClass().getName(), ex.getMessage(), body);
        logger.warn(message);

        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
