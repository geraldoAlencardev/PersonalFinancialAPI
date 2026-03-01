package com.Alencar.demo.service;

import com.Alencar.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.Transaction;
import com.Alencar.demo.model.User;
import com.Alencar.demo.model.enums.TransactionStatus;
import com.Alencar.demo.model.enums.TransactionType;
import com.Alencar.demo.repository.AccountRepository;
import com.Alencar.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryService categoryService;

    @Transactional
    public Transaction createTransaction(Transaction transaction, Long userId) {
        Account account = accountRepository.findByIdAndUserWithLock(transaction.getAccount().getId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou não pertence ao usuário"));

        categoryService.findById(transaction.getCategory().getId(),userId);

        if (transaction.getStatus() == null) {
            transaction.setStatus(TransactionStatus.PAID);
        }

        transaction.setUser(User.builder().id(userId).build());

        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        account.setBalance(currentBalance.add(transaction.getSignedAmount()));

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id, Long userId) {
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        Account account = accountRepository.findByIdAndUserWithLock(transaction.getAccount().getId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta associada não encontrada"));

        BigDecimal restoredBalance = account.getBalance().subtract(transaction.getSignedAmount());
        account.setBalance(restoredBalance);

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

    @Transactional(readOnly = true)
    public List<Transaction> findByAccountIdAndUser(Long accountId, Long userId) {
        Account account = accountRepository.findByIdAndUser(accountId, User.builder().id(userId).build())
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou não pertence ao usuário"));

        return transactionRepository.findByAccountIdAndUser(accountId, account.getUser());
    }

    public List<Transaction> findByFilters(Long userId, TransactionType type, Long accountId, LocalDate start, LocalDate end) {
        User user = User.builder().id(userId).build();

        if (type != null) {
            return transactionRepository.findByUserAndType(user, type);
        } else if (start != null && end != null) {
            return transactionRepository.findByUserAndDateBetween(user, start, end);
        } else if (accountId != null) {
            return transactionRepository.findByAccountIdAndUser(accountId, user);
        }

        return transactionRepository.findByUserOrderByDateDesc(user);
    }

}
