package com.vaka.daily.repository;

import com.vaka.daily.domain.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskTypeRepository extends JpaRepository<TaskType, Integer> {
    Optional<TaskType> findByName(String name);
}
