package com.example.week8_day6.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddTagRequest(
        @NotBlank(message = "Tag name must not be empty")
        @Size(max = 30, message = "Tag name cannot exceed 30 characters")
        String tag
) {
}
