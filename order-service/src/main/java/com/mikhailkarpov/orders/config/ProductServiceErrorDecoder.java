package com.mikhailkarpov.orders.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
public class ProductServiceErrorDecoder implements ErrorDecoder {

    private static final ErrorDecoder DEFAULT = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {

        log.warn("%s resulted in d: %s", methodKey, response.status(), getResponseBody(response));
        return DEFAULT.decode(methodKey, response);
    }

    private String getResponseBody(Response response) {

        try (BufferedReader reader = new BufferedReader(response.body().asReader(StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));

        } catch (IOException e) {
            log.error("Failed to read feign error response body: {}", e.getMessage());
            return String.format("%d error", response.status());
        }
    }
}
