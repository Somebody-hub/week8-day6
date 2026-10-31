package com.example.week8_day6.dto;

import com.example.week8_day6.model.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required and cannot be empty")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String title,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @NotNull(message = "Priority is required")
        TaskPriority priority) {
}
