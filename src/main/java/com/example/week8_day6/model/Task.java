package com.example.week8_day6.model;


import com.example.week8_day6.exception.StatusTransitionException;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Generated;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, name = "task_priority")
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30, name = "task_status")
    private TaskStatus status;

    @ElementCollection
    @CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "tag", nullable = false, length = 30)
    @BatchSize(size = 50)
    private Set<String> tags = new HashSet<>();

    @Generated
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private OffsetDateTime createdAt;



    protected Task() {}

    public Task(String title, String description, TaskPriority taskPriority) {
        setTitle(title);
        this.description = description;
        setPriority(taskPriority);
        this.status = TaskStatus.NEW;
    }


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public OffsetDateTime getCreatedAt() { return createdAt; }

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Task title cannot be empty or null");
        }
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(TaskPriority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Task priority cannot be null");
        }
        this.priority = priority;
    }

    /*
    private void setStatus(TaskStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Task status cannot be null");
        }
        this.status = status;
    }

     */

    public void changeStatus(TaskStatus status) {
        if (!this.status.canTransitionTo(status)) {
            throw new StatusTransitionException(this.status, status);
        }
        this.status = status;
    }

    public boolean addTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("The tag cannot be empty");
        }
        return this.tags.add(tag.trim().toLowerCase());
    }

    public void removeTag(String tag) {
        if (tag != null) {
            this.tags.remove(tag.trim().toLowerCase());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;
        if (o instanceof Task task){
            return id != null && id.equals(task.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
