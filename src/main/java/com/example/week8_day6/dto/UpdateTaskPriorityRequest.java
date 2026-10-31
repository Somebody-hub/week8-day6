package com.example.week8_day6.dto;

import com.example.week8_day6.model.TaskPriority;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskPriorityRequest(
        @NotNull(message = "Priority is required")
        TaskPriority priority) {
}
