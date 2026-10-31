package com.example.week8_day6.dto;

import com.example.week8_day6.model.Task;
import org.springframework.data.domain.Page;

import java.util.List;

public record TaskPageResponse(
        List<TaskResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static TaskPageResponse from(Page<TaskResponse> springPage) {
        return new TaskPageResponse(
                springPage.getContent(),
                springPage.getNumber(),
                springPage.getSize(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }
}