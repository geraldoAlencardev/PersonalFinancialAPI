package com.Alencar.demo.mapper;

import com.Alencar.demo.dto.transaction.TransactionCreateDTO;
import com.Alencar.demo.dto.transaction.TransactionResponseDTO;
import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.Category;
import com.Alencar.demo.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionCreateDTO dto) {

        Category category = Category.builder().id(dto.categoryId()).build();
        Account account = Account.builder().id(dto.accountId()).build();

        return Transaction.builder()
                .amount(dto.amount())
                .date(dto.date())
                .description(dto.description())
                .type(dto.type())
                .category(category)
                .account(account)
                .build();
    }

    public TransactionResponseDTO TransactionResponseDTO(Transaction entity) {
        return new TransactionResponseDTO(
                entity.getId(),
                entity.getSignedAmount(),
                entity.getDate(),
                entity.getDescription(),
                entity.getType(),
                entity.getCategory() != null ? entity.getCategory().getName() : "Sem categoria",
                entity.getAccount() != null ? entity.getAccount().getName() : "Conta desconhecida"
        );
    }
}
