package com.vaka.daily_notifier.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @NotNull
    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotNull
    private LocalDateTime deadline;

    private Boolean status;

    @NotNull
    private Integer scheduleId;

    @NotNull
    private Integer taskTypeId;
}
