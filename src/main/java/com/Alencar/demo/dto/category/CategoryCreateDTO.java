package com.Alencar.demo.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryCreateDTO(
        @NotBlank(message = "O nome da categoria é obrigatório")
        String name,
        String icon,
        String color
) {}
