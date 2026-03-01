package com.Alencar.demo.controller;

import com.Alencar.demo.dto.transaction.TransactionCreateDTO;
import com.Alencar.demo.dto.transaction.TransactionResponseDTO;
import com.Alencar.demo.mapper.TransactionMapper;
import com.Alencar.demo.model.Transaction;
import com.Alencar.demo.model.enums.TransactionType;
import com.Alencar.demo.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;


    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(@RequestHeader("X-User-Id") Long userId,
                                                                    @Valid @RequestBody TransactionCreateDTO dto){
        Transaction transaction = transactionMapper.toEntity(dto);
        Transaction savedTransaction = transactionService.createTransaction(transaction, userId);
        TransactionResponseDTO response = transactionMapper.TransactionResponseDTO(savedTransaction);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> findAll(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam(required = false)TransactionType type,
            @RequestParam(required = false)Long accountId,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
            @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate
            ){
        List<Transaction> transactions = transactionService.findByFilters(userId, type, accountId, startDate, endDate);

        List<TransactionResponseDTO> response = transactions.stream().map(transactionMapper::TransactionResponseDTO).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> findByAccountAndUser(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long accountId
    ){
        List<Transaction> transactions = transactionService.findByAccountIdAndUser(userId, accountId);
        List<TransactionResponseDTO> response = transactions.stream().map(transactionMapper::TransactionResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id){
        transactionService.deleteTransaction(id, userId);
        return ResponseEntity.noContent().build();
    }
}
