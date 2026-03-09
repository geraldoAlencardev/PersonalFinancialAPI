package com.Alencar.demo.controller;

import com.Alencar.demo.dto.account.AccountCreateDTO;
import com.Alencar.demo.dto.account.AccountResponseDTO;
import com.Alencar.demo.dto.account.TotalBalanceResponseDTO;
import com.Alencar.demo.mapper.AccountMapper;
import com.Alencar.demo.model.Account;
import com.Alencar.demo.model.User;
import com.Alencar.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> findAllByUser(
            @AuthenticationPrincipal User loggedUser
    ){
        List<Account> accounts = accountService.findAllByUser(loggedUser.getId());
        List<AccountResponseDTO> response = accounts.stream().map(accountMapper::accountResponseDTO).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponseDTO> findById(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Long accountId){

        Account account = accountService.findByIdAndUser(loggedUser.getId(), accountId);
        AccountResponseDTO response = accountMapper.accountResponseDTO(account);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/totalBalance")
    public ResponseEntity<TotalBalanceResponseDTO> getTotalBalance(
            @AuthenticationPrincipal User loggedUser
    ){
        BigDecimal totalBalance = accountService.getTotalBalance(loggedUser.getId());
        TotalBalanceResponseDTO response = new TotalBalanceResponseDTO(totalBalance);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(
            @AuthenticationPrincipal User loggedUser,
            @Valid @RequestBody AccountCreateDTO dto
            ){
        Account account = accountMapper.toEntity(dto);
        Account savedAccount = accountService.createAccount(account, loggedUser.getId());
        AccountResponseDTO response = accountMapper.accountResponseDTO(savedAccount);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(
            @AuthenticationPrincipal User loggedUser,
            @PathVariable Long id
    ){
        accountService.deleteAccount(loggedUser.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
