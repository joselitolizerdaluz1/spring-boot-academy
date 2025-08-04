# Lab 4: Introduction to Spring Transactions

## Difficulty Level
**Easy**

## Learning Objectives
- Understand the concept of database transactions and ACID properties
- Learn how to enable transaction management in Spring Boot
- Implement basic transaction management using `@Transactional`
- Understand transaction propagation and isolation levels
- Learn to handle transaction rollbacks and exceptions
- Identify when transactions are needed in applications

## Scenario
You're building a banking application where you need to transfer money between accounts. This operation requires updating multiple database records atomically - if any part fails, all changes should be rolled back to maintain data consistency. You need to implement proper transaction management to ensure the integrity of financial operations.

## Initial Project Setup Guidance

### Dependencies Required
Add these to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Step-by-step Instructions

### 1. Project Setup
1. Create a new Spring Boot project with the dependencies listed above
2. Create the necessary package structure for entities, repositories, and services
3. Set up the database configuration

### 2. Create the Account Entity
Create `src/main/java/com/example/transactionlab/entity/Account.java`:

```java
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
    
    // Constructors
    public Account() {}
    
    public Account(String accountNumber, String accountHolder, BigDecimal balance, AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;
        this.accountType = accountType;
    }
    
    // Getters and Setters
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
}
```

### 3. Create the Transaction Record Entity
Create `src/main/java/com/example/transactionlab/entity/TransactionRecord.java`:

```java
package com.example.transactionlab.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_records")
public class TransactionRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;
    
    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private Account toAccount;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    
    private LocalDateTime timestamp;
    
    public enum TransactionType {
        TRANSFER, DEPOSIT, WITHDRAWAL
    }
    
    // Constructors
    public TransactionRecord() {}
    
    public TransactionRecord(Account fromAccount, Account toAccount, BigDecimal amount, TransactionType transactionType) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Account getFromAccount() { return fromAccount; }
    public void setFromAccount(Account fromAccount) { this.fromAccount = fromAccount; }
    
    public Account getToAccount() { return toAccount; }
    public void setToAccount(Account toAccount) { this.toAccount = toAccount; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public TransactionType getTransactionType() { return transactionType; }
    public void setTransactionType(TransactionType transactionType) { this.transactionType = transactionType; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
```

### 4. Create the Repository Interfaces
Create `src/main/java/com/example/transactionlab/repository/AccountRepository.java`:

```java
package com.example.transactionlab.repository;

import com.example.transactionlab.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);
}
```

Create `src/main/java/com/example/transactionlab/repository/TransactionRecordRepository.java`:

```java
package com.example.transactionlab.repository;

import com.example.transactionlab.entity.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, Long> {
}
```

### 5. Create the Banking Service with Transactions
Create `src/main/java/com/example/transactionlab/service/BankingService.java`:

```java
package com.example.transactionlab.service;

import com.example.transactionlab.entity.Account;
import com.example.transactionlab.entity.TransactionRecord;
import com.example.transactionlab.repository.AccountRepository;
import com.example.transactionlab.repository.TransactionRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    
    public BankingService(AccountRepository accountRepository, 
                         TransactionRecordRepository transactionRecordRepository) {
        this.accountRepository = accountRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }
    
    @Transactional
    public void transferMoney(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        // Validate amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        // Find accounts with pessimistic locking to prevent race conditions
        Account fromAccount = accountRepository.findByAccountNumberWithLock(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("From account not found: " + fromAccountNumber));
        
        Account toAccount = accountRepository.findByAccountNumberWithLock(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("To account not found: " + toAccountNumber));
        
        // Check sufficient balance
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance in account: " + fromAccountNumber);
        }
        
        // Perform the transfer
        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));
        
        // Save the updated accounts
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
        
        // Record the transaction
        TransactionRecord transaction = new TransactionRecord(
                fromAccount, toAccount, amount, TransactionRecord.TransactionType.TRANSFER);
        transactionRecordRepository.save(transaction);
    }
    
    @Transactional(readOnly = true)
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found: " + accountNumber));
    }
    
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
    
    @Transactional
    public Account createAccount(String accountNumber, String accountHolder, 
                               BigDecimal initialBalance, Account.AccountType accountType) {
        // Check if account already exists
        if (accountRepository.findByAccountNumber(accountNumber).isPresent()) {
            throw new RuntimeException("Account already exists: " + accountNumber);
        }
        
        Account account = new Account(accountNumber, accountHolder, initialBalance, accountType);
        return accountRepository.save(account);
    }
    
    @Transactional(readOnly = true)
    public List<TransactionRecord> getTransactionHistory(String accountNumber) {
        Account account = getAccount(accountNumber);
        return transactionRecordRepository.findAll().stream()
                .filter(t -> t.getFromAccount().equals(account) || t.getToAccount().equals(account))
                .toList();
    }
}
```

### 6. Create the Controller
Create `src/main/java/com/example/transactionlab/controller/BankingController.java`:

```java
package com.example.transactionlab.controller;

import com.example.transactionlab.entity.Account;
import com.example.transactionlab.entity.TransactionRecord;
import com.example.transactionlab.service.BankingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/banking")
public class BankingController {
    
    private final BankingService bankingService;
    
    public BankingController(BankingService bankingService) {
        this.bankingService = bankingService;
    }
    
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> request) {
        String accountNumber = (String) request.get("accountNumber");
        String accountHolder = (String) request.get("accountHolder");
        BigDecimal initialBalance = new BigDecimal(request.get("initialBalance").toString());
        Account.AccountType accountType = Account.AccountType.valueOf(
                request.get("accountType").toString().toUpperCase());
        
        Account account = bankingService.createAccount(accountNumber, accountHolder, 
                                                     initialBalance, accountType);
        return ResponseEntity.ok(account);
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(bankingService.getAllAccounts());
    }
    
    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getAccount(accountNumber));
    }
    
    @PostMapping("/transfer")
    public ResponseEntity<String> transferMoney(@RequestBody Map<String, Object> request) {
        String fromAccount = (String) request.get("fromAccount");
        String toAccount = (String) request.get("toAccount");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        
        bankingService.transferMoney(fromAccount, toAccount, amount);
        return ResponseEntity.ok("Transfer completed successfully");
    }
    
    @GetMapping("/accounts/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionRecord>> getTransactionHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankingService.getTransactionHistory(accountNumber));
    }
}
```

### 7. Configure Application Properties
Create `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:bankingdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA configuration
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Transaction configuration
spring.jpa.properties.hibernate.connection.isolation=READ_COMMITTED

# Actuator configuration
management.endpoints.web.exposure.include=*
management.endpoint.health.show-details=always

# Application info
info.app.name=Spring Transactions Lab
info.app.description=Learning Spring Transaction Management
info.app.version=4.0.0
```

### 8. Create Data Initializer
Create `src/main/java/com/example/transactionlab/config/DataInitializer.java`:

```java
package com.example.transactionlab.config;

import com.example.transactionlab.entity.Account;
import com.example.transactionlab.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final AccountRepository accountRepository;
    
    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create some test accounts
        if (accountRepository.count() == 0) {
            Account account1 = new Account("ACC001", "John Doe", 
                    new BigDecimal("1000.00"), Account.AccountType.SAVINGS);
            Account account2 = new Account("ACC002", "Jane Smith", 
                    new BigDecimal("500.00"), Account.AccountType.CHECKING);
            Account account3 = new Account("ACC003", "Bob Johnson", 
                    new BigDecimal("2500.00"), Account.AccountType.BUSINESS);
            
            accountRepository.save(account1);
            accountRepository.save(account2);
            accountRepository.save(account3);
            
            System.out.println("Test accounts created successfully!");
        }
    }
}
```

### 9. Run and Test
```bash
./gradlew bootRun
```

### 10. Test Transactions
```bash
# View all accounts
curl http://localhost:8080/api/banking/accounts

# Transfer money between accounts
curl -X POST http://localhost:8080/api/banking/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromAccount":"ACC001","toAccount":"ACC002","amount":100.00}'

# Check account balances after transfer
curl http://localhost:8080/api/banking/accounts/ACC001
curl http://localhost:8080/api/banking/accounts/ACC002

# View transaction history
curl http://localhost:8080/api/banking/accounts/ACC001/transactions

# Try to transfer more than available balance (should fail)
curl -X POST http://localhost:8080/api/banking/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromAccount":"ACC002","toAccount":"ACC001","amount":1000.00}'
```

## Verification Steps

### 1. Transaction Verification
- ✅ Confirm that successful transfers update both account balances
- ✅ Verify that failed transfers don't change any account balances
- ✅ Check that transaction records are created for successful transfers
- ✅ Test that insufficient balance transfers are properly rolled back

### 2. Expected Output Examples

**Account list response:**
```json
[
  {
    "id": 1,
    "accountNumber": "ACC001",
    "accountHolder": "John Doe",
    "balance": 900.00,
    "accountType": "SAVINGS"
  },
  {
    "id": 2,
    "accountNumber": "ACC002",
    "accountHolder": "Jane Smith",
    "balance": 600.00,
    "accountType": "CHECKING"
  }
]
```

**Transfer response:**
```json
"Transfer completed successfully"
```

### 3. Database Verification
1. Access H2 console at `http://localhost:8080/h2-console`
2. Connect with JDBC URL: `jdbc:h2:mem:bankingdb`
3. Verify that account balances are updated correctly
4. Check that transaction records are created

## Reflection Questions

1. **What are the ACID properties and why are they important for transactions?**
   - Atomicity: All operations succeed or all fail
   - Consistency: Database remains in valid state
   - Isolation: Concurrent transactions don't interfere
   - Durability: Committed changes are permanent

2. **Why is pessimistic locking used in the transfer method?**
   - Prevents race conditions when multiple transfers happen simultaneously
   - Ensures account balances are consistent during concurrent access
   - Prevents double-spending scenarios

3. **What happens if an exception occurs during a transfer?**
   - Spring automatically rolls back the transaction
   - All database changes are undone
   - Account balances remain unchanged
   - No transaction record is created

4. **How could you improve the transaction management in this application?**
   - Add more specific exception handling
   - Implement retry mechanisms for transient failures
   - Add transaction timeouts
   - Use different isolation levels for different operations

## Key Concepts Covered

- **@Transactional Annotation**: Core annotation for transaction management
- **Transaction Propagation**: How transactions behave when nested
- **Transaction Isolation**: How concurrent transactions interact
- **ACID Properties**: Atomicity, Consistency, Isolation, Durability
- **Pessimistic Locking**: Preventing concurrent access conflicts
- **Transaction Rollback**: Automatic rollback on exceptions
- **Read-Only Transactions**: Optimizing read operations
- **Database Consistency**: Maintaining data integrity

## Resources

- [Spring Transaction Management](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#transaction)
- [Baeldung: Spring Transaction Management](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring)
- [Spring Boot Data JPA](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.jpa-and-spring-data)
- [Hibernate Transaction Management](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#transactions)

## Next Steps
After completing this lab, you should be comfortable with:
- Basic transaction management with `@Transactional`
- Understanding ACID properties
- Implementing simple transaction scenarios
- Handling transaction rollbacks

This foundation prepares you for more advanced transaction concepts like propagation, isolation levels, and distributed transactions in subsequent labs. 