package com.example.transactionlab.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String accountNumber;
    
    @NotBlank
    private String accountHolder;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal balance;
    
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    
    public enum AccountType {
        SAVINGS, CHECKING, BUSINESS
    }
    
    // TODO 1: Add constructors
    // TODO 2: Uncomment the following constructors:
    /*
    public Account() {}
    
    public Account(String accountNumber, String accountHolder, BigDecimal balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.accountType = accountType;
    }
    */
    
    // TODO 3: Add getters and setters
    // TODO 4: Uncomment the following getters and setters:
    /*
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    
    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }
    
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    */
} 