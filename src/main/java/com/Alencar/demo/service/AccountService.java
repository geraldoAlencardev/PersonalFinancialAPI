package com.Alencar.demo.service;

import com.Alencar.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.User;
import com.Alencar.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public List<Account> findAllByUser(Long userId) {
        User user = User.builder().id(userId).build();
        return accountRepository.findByUser(user);
    }

    @Transactional
    public Account createAccount(Account account) {
        if(account.getBalance() == null){
            account.setBalance(BigDecimal.ZERO);
        }
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Account findByIdAndUser(Long userId, Long accountId) {
        User user = User.builder().id(userId).build();
        return accountRepository.findByIdAndUser(accountId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada"));
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalBalance(Long userId) {
        List<Account> accounts = findAllByUser(userId);
        return accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


}
