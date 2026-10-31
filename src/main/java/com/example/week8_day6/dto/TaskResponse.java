package com.example.week8_day6.dto;

import com.example.week8_day6.model.TaskPriority;
import com.example.week8_day6.model.TaskStatus;

import java.util.Set;

public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskPriority priority,
        TaskStatus status,
        Set<String> tags) {
}
