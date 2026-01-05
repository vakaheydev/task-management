package com.vaka.daily_notifier.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TaskType {
    @NotNull
    private int id;

    @NotEmpty
    private String name;

    public TaskType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public TaskType() {
    }
}
