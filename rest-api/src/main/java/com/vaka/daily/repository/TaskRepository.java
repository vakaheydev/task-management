package com.vaka.daily.repository;

import com.vaka.daily.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    @Query("select t from Task t where t.isCompleted = false and t.deadline < :now")
    List<Task> findExpiredTasks(@Param("now") LocalDateTime now);

    @Query("SELECT t FROM Task t WHERE t.schedule.user.id = :userId AND LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Task> findByUserIdAndNameContaining(@Param("userId") Integer userId, @Param("name") String name);
}
