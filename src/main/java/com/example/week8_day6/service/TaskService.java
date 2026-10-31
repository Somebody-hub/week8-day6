package com.example.week8_day6.service;

import com.example.week8_day6.dto.*;
import com.example.week8_day6.exception.TagAlreadyExistsException;
import com.example.week8_day6.exception.TaskNotFoundException;
import com.example.week8_day6.model.Task;
import com.example.week8_day6.model.TaskPriority;
import com.example.week8_day6.model.TaskStatus;
import com.example.week8_day6.projection.PriorityCount;
import com.example.week8_day6.projection.StatusCount;
import com.example.week8_day6.repository.TaskRepository;
import com.example.week8_day6.repository.TaskSpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskPageResponse getTasks(TaskStatus status, TaskPriority priority, String tag, String q, Pageable pageable) {
        Specification<Task> spec = Specification.allOf(
                TaskSpecifications.hasStatus(status),
                TaskSpecifications.hasPriority(priority),
                TaskSpecifications.hasTag(tag),
                TaskSpecifications.titleContain(q)
        );

        Page<TaskResponse> page = taskRepository.findAll(spec, pageable)
                .map(this::toResponse);
        return TaskPageResponse.from(page);
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task newTask = taskRepository.save(
                new Task(request.title(), request.description(), request.priority()));
        return toResponse(newTask);
    }

    @Transactional
    public TaskResponse updateStatus(Long id, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findWithTagsById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.changeStatus(request.status());
        return toResponse(task);
    }

    @Transactional
    public TaskResponse updatePriority(Long id, UpdateTaskPriorityRequest request) {
        Task task = taskRepository.findWithTagsById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.setPriority(request.priority());
        return toResponse(task);
    }

    public TaskResponse findById(Long id) {
        Task task = taskRepository.findWithTagsById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return toResponse(task);
    }

    @Transactional
    public void deleteById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponse addTag(Long id, String tag) {
        Task task = taskRepository.findWithTagsById(id).orElseThrow(() -> new TaskNotFoundException(id));
        if (task.getTags().contains(tag.trim().toLowerCase())) {
            throw new TagAlreadyExistsException(tag);
        }
        task.addTag(tag);
        return toResponse(task);
    }

    @Transactional
    public void removeTag(Long id, String tag) {
        Task task = taskRepository.findWithTagsById(id).orElseThrow(() -> new TaskNotFoundException(id));
        task.removeTag(tag);
    }

    public TaskStatisticsResponse getStatistics() {
        Map<TaskStatus, Long> byStatus = new EnumMap<>(TaskStatus.class);
        for (TaskStatus status : TaskStatus.values()) {
            byStatus.put(status, 0L);
        }
        for (StatusCount statusCount : taskRepository.countByStatus()) {
            byStatus.put(statusCount.getStatus(), statusCount.getTotal());
        }

        Map<TaskPriority, Long> byPriority = new EnumMap<>(TaskPriority.class);
        for (TaskPriority priority : TaskPriority.values()) {
            byPriority.put(priority, 0L);
        }
        for (PriorityCount priorityCount : taskRepository.countByPriority()) {
            byPriority.put(priorityCount.getPriority(), priorityCount.getTotal());
        }

        return new TaskStatisticsResponse(byStatus, byPriority);
    }


    private TaskResponse toResponse(Task task) {
        return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getPriority(), task.getStatus(), new HashSet<>(task.getTags()));
    }
}
