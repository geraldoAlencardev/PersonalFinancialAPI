package com.Alencar.demo.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordUpdateDTO(
        @NotBlank String currentPassword,
        @NotBlank String newPassword,
        @NotBlank String confirmPassword
) {}
