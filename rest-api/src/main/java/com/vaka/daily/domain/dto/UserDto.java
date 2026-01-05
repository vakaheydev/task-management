package com.vaka.daily.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotEmpty
    @Size(max = 100)
    private String login;

    @NotEmpty
    @Size(max = 100)
    private String firstName;

    @NotEmpty
    @Size(max = 100)
    private String password;

    @NotEmpty
    @Size(max = 100)
    private String secondName;

    @Size(max = 100)
    private String patronymic;
}
