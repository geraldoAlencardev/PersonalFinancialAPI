package com.Alencar.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_accounts")
    @SequenceGenerator(name = "seq_accounts", sequenceName = "seq_accounts", allocationSize = 1)
    @EqualsAndHashCode.Include
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "name", nullable = false,length = 50)
    private String name;

    @Column(name = "balance",  nullable = false, precision = 19, scale = 2)
    private BigDecimal balance =  BigDecimal.ZERO;

    @Column(length = 7)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
