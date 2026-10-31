package com.example.week8_day6.controller;

import com.example.week8_day6.dto.*;
import com.example.week8_day6.model.TaskPriority;
import com.example.week8_day6.model.TaskStatus;
import com.example.week8_day6.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(TaskController.BASE_PATH)
public class TaskController {
    public static final String BASE_PATH = "/api/tasks";
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<TaskPageResponse> getTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String q,
            Pageable pageable
            ) {
        return ResponseEntity.ok(taskService.getTasks(status, priority, tag, q, pageable));
    }

    @GetMapping("/statistics")
    public ResponseEntity<TaskStatisticsResponse> getStatistics() {
        return ResponseEntity.ok(taskService.getStatistics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse createdTask = taskService.create(request);
        return ResponseEntity
                .created(URI.create(BASE_PATH + "/" + createdTask.id()))
                .body(createdTask);
    }

    @PostMapping("/{id}/tags")
    public ResponseEntity<TaskResponse> addTag(
            @PathVariable Long id,
            @Valid @RequestBody AddTagRequest request
    ) {
        TaskResponse updateTask = taskService.addTag(id, request.tag());
        return ResponseEntity.status(HttpStatus.CREATED).body(updateTask);
    }

    @DeleteMapping("/{id}/tags/{tag}")
    public ResponseEntity<Void> removeTag(
            @PathVariable Long id,
            @PathVariable String tag
    ) {
        taskService.removeTag(id, tag);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskResponse> updatePriority(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateTaskPriorityRequest request
    ) {
        TaskResponse updateTask = taskService.updatePriority(id, request);
        return ResponseEntity.ok(updateTask);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(@PathVariable Long id,
                                                     @Valid @RequestBody UpdateTaskStatusRequest request
    ) {
        TaskResponse updateTask = taskService.updateStatus(id, request);
        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
