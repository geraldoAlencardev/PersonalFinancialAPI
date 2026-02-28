package com.Alencar.demo.model;

import com.Alencar.demo.model.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trans")
    @SequenceGenerator(name = "seq_trans", sequenceName = "seq_trans", allocationSize = 1)
    @EqualsAndHashCode.Include
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(name = "description",  nullable = false, length = 150)
    private String description;

    @Positive(message = "O valor deve ser maior que zero")
    @Column(name = "amount",  nullable = false, scale = 2, precision = 19)
    private BigDecimal amount;

    @Column(name = "date",  nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Account account;

    @Transient
    @JsonIgnore
    public BigDecimal getSignedAmount() {
        if(amount == null) return BigDecimal.ZERO;
        return type == TransactionType.EXPENSE ? amount.negate() : amount;
    }
}
