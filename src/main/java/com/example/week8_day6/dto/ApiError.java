package com.example.week8_day6.dto;

import java.time.OffsetDateTime;

public record ApiError(
        String code,
        String message,
        String path,
        OffsetDateTime timestamp
) {
    public ApiError(String code, String message, String path) {
        this(code, message, path, OffsetDateTime.now());
    }
}
