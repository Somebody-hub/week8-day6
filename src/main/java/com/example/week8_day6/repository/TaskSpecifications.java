package com.example.week8_day6.repository;

import com.example.week8_day6.model.Task;
import com.example.week8_day6.model.TaskPriority;
import com.example.week8_day6.model.TaskStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;


public final class TaskSpecifications {
    private TaskSpecifications() {
    }

    public static Specification<Task> hasStatus(TaskStatus taskStatus) {
        return ((root, query, criteriaBuilder) ->
                taskStatus == null ? null : criteriaBuilder.equal(root.get("status"), taskStatus));
    }

    public static Specification<Task> hasPriority(TaskPriority priority) {
        return (root, query, criteriaBuilder) ->
                priority == null ? null : criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> hasTag(String tag) {
        return (root, query, cb) -> {
            if (tag == null || tag.isBlank()) {
                return null;
            }
            query.distinct(true);
            Join<Task, String> tagsJoin = root.join("tags");
            return cb.equal(cb.lower(tagsJoin), tag.trim().toLowerCase());
        };
    }

    public static Specification<Task> titleContain(String query) {
        return (root, query1, criteriaBuilder) ->
                ((query == null) || (query.isBlank()))
                        ? null
                        :criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + query.trim().toLowerCase() + "%");
    }
}
