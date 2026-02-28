package com.Alencar.demo.repository;

import com.Alencar.demo.model.Transaction;
import com.Alencar.demo.model.User;
import com.Alencar.demo.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUserOrderByDateDesc(User user);

    List<Transaction> findByUserAndType(User user, TransactionType type);

    List<Transaction> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    List<Transaction> findByAccountIdAndUser(Long accountId, User user);

    List<Transaction> findByUserAndDescriptionContainingIgnoreCase(User user, String description);
}
