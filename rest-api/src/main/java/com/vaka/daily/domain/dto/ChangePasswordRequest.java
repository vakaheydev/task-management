package com.vaka.daily.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordRequest {
    @NotEmpty
    private String oldPassword;

    @NotEmpty
    private String newPassword;
}
