package com.mikhailkarpov.customers.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhailkarpov.customers.dto.ApiErrorResponse;
import com.mikhailkarpov.customers.exception.ConflictException;
import com.netflix.hystrix.exception.HystrixBadRequestException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@AllArgsConstructor
public class AuthServiceErrorDecoder implements ErrorDecoder {

    private static final ErrorDecoder DEFAULT = new ErrorDecoder.Default();
    private final ObjectMapper objectMapper;

    @Override
    public Exception decode(String methodKey, Response response) {

        if (response.status() == 409) {
            String message = errorResponse(response);
            return new HystrixBadRequestException("Auth-service exception", new ConflictException(message));

        } else {
            return DEFAULT.decode(methodKey, response);
        }
    }

    private String errorResponse(Response response) {

        try (InputStream inputStream = response.body().asInputStream()) {

            ApiErrorResponse apiErrorResponse = objectMapper.readValue(inputStream, ApiErrorResponse.class);
            return apiErrorResponse.getMessage();

        } catch (IOException e) {
            log.error("Failed to read feign error response: {}", e);
            return null;
        }
    }

}
