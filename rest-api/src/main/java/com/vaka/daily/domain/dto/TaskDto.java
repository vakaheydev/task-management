package com.vaka.daily.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotEmpty
    @Size(max = 100)
    private String description;

    @NotNull
    private LocalDateTime deadline;

    @NotNull
    @Builder.Default
    private Boolean status = true;

    @NotNull
    private Integer scheduleId;

    @NotNull
    private Integer taskTypeId;
}
