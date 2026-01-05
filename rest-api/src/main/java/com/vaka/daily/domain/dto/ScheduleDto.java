package com.vaka.daily.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    private Integer id;

    @NotEmpty
    @Size(max = 100)
    private String name;

    @NotNull
    private Integer userId;

    private List<TaskDto> tasks;
}
