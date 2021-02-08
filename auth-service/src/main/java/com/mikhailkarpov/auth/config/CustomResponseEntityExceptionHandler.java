package com.mikhailkarpov.auth.config;

import com.mikhailkarpov.auth.dto.ApiErrorResponse;
import com.mikhailkarpov.auth.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.auth.exception.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

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

}
