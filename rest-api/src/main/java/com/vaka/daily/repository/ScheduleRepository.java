package com.vaka.daily.repository;

import com.vaka.daily.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    List<Schedule> findByName(String name);

    @Query("select s from Schedule s join s.user u where u.id = :user_id")
    List<Schedule> findByUserId(@Param("user_id") Integer id);
}
