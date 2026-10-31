package com.example.week8_day6.repository;


import com.example.week8_day6.model.Task;
import com.example.week8_day6.projection.PriorityCount;
import com.example.week8_day6.projection.StatusCount;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;


public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @EntityGraph(attributePaths = "tags")
    Optional<Task> findWithTagsById(Long id);

    @Query("select t.status as status, count(t) as total from Task t group by t.status")
    List<StatusCount> countByStatus();

    @Query("select t.priority as priority, count(t) as total from Task t group by t.priority")
    List<PriorityCount> countByPriority();

}
