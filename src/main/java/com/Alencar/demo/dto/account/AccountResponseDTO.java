package com.Alencar.demo.dto.account;

import java.math.BigDecimal;

public record AccountResponseDTO(
        Long id,
        String name,
        BigDecimal balance
) {}
