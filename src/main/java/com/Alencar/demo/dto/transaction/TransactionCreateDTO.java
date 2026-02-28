package com.Alencar.demo.dto.transaction;

import com.Alencar.demo.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreateDTO(
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser positivo")
        BigDecimal amount,

        @NotNull(message = "A data é obrigatória")
        LocalDate date,

        @NotNull(message = "O tipo da transação é obrigatório")
        TransactionType type,

        String description,

        @NotNull(message = "O ID da categoria é obrigatório")
        Long categoryId,

        @NotNull(message = "O ID da conta é obrigatório")
        Long accountId
) {}
