package com.example.week8_day6.dto;

import com.example.week8_day6.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskStatusRequest(
        @NotNull(message = "Status is required")
        TaskStatus status) {
}
