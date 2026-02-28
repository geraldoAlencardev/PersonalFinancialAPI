package com.Alencar.demo.dto.account;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record AccountCreateDTO(
        @NotBlank(message = "O nome da conta é obrigatório")
        String name,

        BigDecimal balance
) {}
