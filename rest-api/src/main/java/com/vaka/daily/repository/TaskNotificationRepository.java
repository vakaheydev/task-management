package com.vaka.daily.repository;

import com.vaka.daily.domain.Task;
import com.vaka.daily.domain.TaskNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskNotificationRepository extends JpaRepository<TaskNotification, Integer> {
    @Query("select tn from TaskNotification tn where tn.task.id = :taskId")
    Optional<TaskNotification> findByTaskId(@Param("taskId") Integer taskId);

    @Query("select t from Task t where " +
            "(t.status = false and t.deadline > :now) or " +
            "(t.status = false and t.taskType.id = 2)")
    List<Task> findTasksForNotification(@Param("now") LocalDateTime now);
}
