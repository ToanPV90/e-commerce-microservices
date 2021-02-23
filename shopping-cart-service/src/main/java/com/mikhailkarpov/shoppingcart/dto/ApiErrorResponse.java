package com.mikhailkarpov.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JSON mapping
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;

    public ApiErrorResponse(String message) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }

    public ApiErrorResponse(Throwable t) {
        this(t.getMessage());
    }
}
