package com.Alencar.demo.mapper;

import com.Alencar.demo.dto.account.AccountCreateDTO;
import com.Alencar.demo.dto.account.AccountResponseDTO;
import com.Alencar.demo.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(AccountCreateDTO dto) {
        return Account.builder()
                .name(dto.name())
                .balance(dto.balance())
                .build();
    }

    public AccountResponseDTO accountResponseDTO(Account entity) {
        return new AccountResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getBalance());
    }
}
