package com.vaka.daily.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "schedule")
@AllArgsConstructor
@Data
@Builder
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    @Column(name = "schedule_name", nullable = false, length = 100)
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "id_user")
    @JsonIgnore
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @JsonProperty("tasksId")
    public List<Integer> getTasksId() {
        return tasks.stream().map(Task::getId).toList();
    }

    @JsonProperty("userId")
    public Integer getUserId() {
        return user.getId();
    }

    public Schedule() {
    }

    public Schedule(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }
}
