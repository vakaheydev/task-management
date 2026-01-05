package com.vaka.daily.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "task_type")
@Data
public class TaskType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_type_id")
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "task_type_name", nullable = false, unique = true, length = 100)
    private String name;

    public TaskType() {
    }

    public TaskType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskType(String name) {
        this.name = name;
    }
}
