package com.vaka.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "Task_Notification")
@Data
public class TaskNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_notification_id")
    private Integer id;

    @Column(name = "last_notified")
    private LocalDateTime lastNotifiedAt;

    @OneToOne
    @JoinColumn(name = "id_task", unique = true)
    @JsonIgnore
    private Task task;
}
