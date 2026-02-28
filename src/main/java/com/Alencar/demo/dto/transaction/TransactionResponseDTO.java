package com.Alencar.demo.dto.transaction;

import com.Alencar.demo.model.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        LocalDate date,
        String description,
        TransactionType type,
        String categoryName,
        String accountName
) {}
