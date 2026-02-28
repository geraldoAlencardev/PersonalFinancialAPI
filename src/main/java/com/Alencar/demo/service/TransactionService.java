package com.Alencar.demo.service;

import com.Alencar.demo.infrastructure.exceptions.ResourceNotFoundException;
import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.Transaction;
import com.Alencar.demo.model.User;
import com.Alencar.demo.model.enums.TransactionStatus;
import com.Alencar.demo.repository.AccountRepository;
import com.Alencar.demo.repository.CategoryRepository;
import com.Alencar.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CategoryService categoryService;

    @Transactional
    public Transaction createTransaction(Transaction transaction, Long userId) {
        // 1. Busca a conta com LOCK e valida se pertence ao usuário (SEGURANÇA + CONCORRÊNCIA)
        Account account = accountRepository.findByIdAndUserWithLock(transaction.getAccount().getId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta não encontrada ou não pertence ao usuário"));

        // 2. Valida se a categoria existe (e se pertence ao usuário ou é global no CategoryService)
        // Isso evita salvar uma transação com ID de categoria inexistente
        categoryService.findById(transaction.getCategory().getId());

        // 3. Garante que a transação tenha um status (CORREÇÃO DE ERRO NO BANCO)
        if (transaction.getStatus() == null) {
            transaction.setStatus(TransactionStatus.PAID);
        }

        // 4. Garante que o usuário da transação seja o usuário logado
        transaction.setUser(User.builder().id(userId).build());

        // 5. Atualiza o saldo da conta
        BigDecimal currentBalance = account.getBalance() != null ? account.getBalance() : BigDecimal.ZERO;
        account.setBalance(currentBalance.add(transaction.getSignedAmount()));

        // 6. Salva (O save da account é implícito pelo @Transactional, mas manter explícito ajuda na clareza)
        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public void deleteTransaction(Long id, Long userId) {
        // Valida se a transação existe e pertence ao usuário antes de deletar
        Transaction transaction = transactionRepository.findById(id)
                .filter(t -> t.getUser().getId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada"));

        // Busca a conta com Lock para reverter o saldo com segurança
        Account account = accountRepository.findByIdAndUserWithLock(transaction.getAccount().getId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Conta associada não encontrada"));

        BigDecimal restoredBalance = account.getBalance().subtract(transaction.getSignedAmount());
        account.setBalance(restoredBalance);

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

    public List<Transaction> findAllByUserId(Long userId) {
        User user = User.builder().id(userId).build();
        return transactionRepository.findByUserOrderByDateDesc(user);
    }

}
