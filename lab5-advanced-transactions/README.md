# Lab 5: Advanced Spring Transaction Concepts

## Difficulty Level
**Advanced**

## Learning Objectives
- Understand transaction propagation behaviors and when to use each
- Implement different transaction isolation levels
- Learn to handle transaction timeouts and rollback rules
- Understand the difference between declarative and programmatic transactions
- Implement nested transactions and understand their behavior
- Learn to debug and troubleshoot transaction issues
- Understand transaction synchronization and resource management

## Scenario
You're building a complex e-commerce system where orders involve multiple services (inventory, payment, shipping, notifications). You need to implement sophisticated transaction management that handles partial failures, retries, and ensures data consistency across multiple operations. The system must handle scenarios where some operations can fail while others succeed, requiring careful transaction design.

## Initial Project Setup Guidance

### Dependencies Required
Add these to your `build.gradle`:

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
    implementation 'org.springframework.boot:spring-boot-starter-actuator'
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    implementation 'com.h2database:h2'
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
    implementation 'org.springframework.retry:spring-retry'
    implementation 'org.springframework.boot:spring-boot-starter-aop'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

## Step-by-step Instructions

### 1. Project Setup
1. Start with the project from Lab 4 or create a new Spring Boot project
2. Add the dependencies listed above
3. Create the necessary package structure for the e-commerce system

### 2. Create the Core Entities
Create `src/main/java/com/example/advancedtransaction/entity/Product.java`:

```java
package com.example.advancedtransaction.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true)
    private String sku;
    
    @NotBlank
    private String name;
    
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;
    
    @NotNull
    private Integer stockQuantity;
    
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    
    public enum ProductStatus {
        ACTIVE, INACTIVE, OUT_OF_STOCK
    }
    
    // Constructors, getters, and setters
    public Product() {}
    
    public Product(String sku, String name, BigDecimal price, Integer stockQuantity) {
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = ProductStatus.ACTIVE;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    public Integer getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    
    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }
}
```

Create `src/main/java/com/example/advancedtransaction/entity/Order.java`:

```java
package com.example.advancedtransaction.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String orderNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public enum OrderStatus {
        PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, FAILED
    }
    
    // Constructors, getters, and setters
    public Order() {}
    
    public Order(String orderNumber, Customer customer) {
        this.orderNumber = orderNumber;
        this.customer = customer;
        this.status = OrderStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    
    public List<OrderItem> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItem> orderItems) { this.orderItems = orderItems; }
    
    public OrderStatus getStatus() { return status; }
    public void setStatus(OrderStatus status) { 
        this.status = status; 
        this.updatedAt = LocalDateTime.now();
    }
    
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
```

Create `src/main/java/com/example/advancedtransaction/entity/Customer.java`:

```java
package com.example.advancedtransaction.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    private String name;
    
    @Email
    @Column(unique = true)
    private String email;
    
    private String phone;
    
    // Constructors, getters, and setters
    public Customer() {}
    
    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
```

### 3. Create Repository Interfaces
Create `src/main/java/com/example/advancedtransaction/repository/ProductRepository.java`:

```java
package com.example.advancedtransaction.repository;

import com.example.advancedtransaction.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findBySku(String sku);
    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.sku = :sku")
    Optional<Product> findBySkuWithLock(@Param("sku") String sku);
}
```

Create `src/main/java/com/example/advancedtransaction/repository/OrderRepository.java`:

```java
package com.example.advancedtransaction.repository;

import com.example.advancedtransaction.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
}
```

### 4. Create Service Layer with Advanced Transaction Management
Create `src/main/java/com/example/advancedtransaction/service/InventoryService.java`:

```java
package com.example.advancedtransaction.service;

import com.example.advancedtransaction.entity.Product;
import com.example.advancedtransaction.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {
    
    private final ProductRepository productRepository;
    
    public InventoryService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void reserveInventory(String sku, int quantity) {
        Product product = productRepository.findBySkuWithLock(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
        
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + sku);
        }
        
        product.setStockQuantity(product.getStockQuantity() - quantity);
        
        if (product.getStockQuantity() == 0) {
            product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
        }
        
        productRepository.save(product);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseInventory(String sku, int quantity) {
        Product product = productRepository.findBySkuWithLock(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
        
        product.setStockQuantity(product.getStockQuantity() + quantity);
        
        if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            product.setStatus(Product.ProductStatus.ACTIVE);
        }
        
        productRepository.save(product);
    }
    
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Product getProduct(String sku) {
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
    }
}
```

Create `src/main/java/com/example/advancedtransaction/service/PaymentService.java`:

```java
package com.example.advancedtransaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PaymentService {
    
    private final Random random = new Random();
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String processPayment(String orderNumber, BigDecimal amount) {
        // Simulate payment processing with potential failure
        if (random.nextInt(10) < 2) { // 20% chance of failure
            throw new RuntimeException("Payment processing failed for order: " + orderNumber);
        }
        
        // Simulate payment processing time
        try {
            Thread.sleep(100 + random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return "PAY-" + orderNumber + "-" + System.currentTimeMillis();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void refundPayment(String paymentId) {
        // Simulate refund processing
        try {
            Thread.sleep(50 + random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

Create `src/main/java/com/example/advancedtransaction/service/NotificationService.java`:

```java
package com.example.advancedtransaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderConfirmation(String orderNumber, String customerEmail) {
        // Simulate sending email notification
        System.out.println("Sending order confirmation email to " + customerEmail + " for order " + orderNumber);
        
        // Simulate potential failure in notification service
        if (Math.random() < 0.1) { // 10% chance of failure
            throw new RuntimeException("Failed to send notification for order: " + orderNumber);
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendOrderCancellation(String orderNumber, String customerEmail) {
        System.out.println("Sending order cancellation email to " + customerEmail + " for order " + orderNumber);
    }
}
```

### 5. Create the Main Order Service with Complex Transaction Logic
Create `src/main/java/com/example/advancedtransaction/service/OrderService.java`:

```java
package com.example.advancedtransaction.service;

import com.example.advancedtransaction.entity.Order;
import com.example.advancedtransaction.entity.OrderItem;
import com.example.advancedtransaction.entity.Product;
import com.example.advancedtransaction.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    
    public OrderService(OrderRepository orderRepository,
                       InventoryService inventoryService,
                       PaymentService paymentService,
                       NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Order createOrder(String customerEmail, Map<String, Integer> items) {
        // Generate order number
        String orderNumber = "ORD-" + UUID.randomUUID().toString().substring(0, 8);
        
        // Create order
        Order order = new Order(orderNumber, new Customer("Customer", customerEmail));
        
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        // Process each item
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String sku = entry.getKey();
            Integer quantity = entry.getValue();
            
            // Get product details
            Product product = inventoryService.getProduct(sku);
            
            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setTotalPrice(product.getPrice().multiply(new BigDecimal(quantity)));
            
            order.getOrderItems().add(orderItem);
            totalAmount = totalAmount.add(orderItem.getTotalPrice());
        }
        
        order.setTotalAmount(totalAmount);
        order.setStatus(Order.OrderStatus.PENDING);
        
        // Save order
        order = orderRepository.save(order);
        
        return order;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void processOrder(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
        
        try {
            // Step 1: Reserve inventory (REQUIRES_NEW - separate transaction)
            for (OrderItem item : order.getOrderItems()) {
                inventoryService.reserveInventory(item.getProduct().getSku(), item.getQuantity());
            }
            
            // Step 2: Process payment (REQUIRES_NEW - separate transaction)
            String paymentId = paymentService.processPayment(orderNumber, order.getTotalAmount());
            
            // Step 3: Update order status
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
            
            // Step 4: Send notification (REQUIRES_NEW - separate transaction)
            try {
                notificationService.sendOrderConfirmation(orderNumber, order.getCustomer().getEmail());
            } catch (Exception e) {
                // Notification failure shouldn't rollback the entire order
                System.err.println("Failed to send notification: " + e.getMessage());
            }
            
        } catch (Exception e) {
            // If any step fails, update order status to FAILED
            order.setStatus(Order.OrderStatus.FAILED);
            orderRepository.save(order);
            
            // Release any reserved inventory
            try {
                for (OrderItem item : order.getOrderItems()) {
                    inventoryService.releaseInventory(item.getProduct().getSku(), item.getQuantity());
                }
            } catch (Exception releaseException) {
                System.err.println("Failed to release inventory: " + releaseException.getMessage());
            }
            
            throw e;
        }
    }
    
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Transactional(readOnly = true)
    public Order getOrder(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
    }
}
```

### 6. Create the Controller
Create `src/main/java/com/example/advancedtransaction/controller/OrderController.java`:

```java
package com.example.advancedtransaction.controller;

import com.example.advancedtransaction.entity.Order;
import com.example.advancedtransaction.entity.Product;
import com.example.advancedtransaction.service.InventoryService;
import com.example.advancedtransaction.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    private final OrderService orderService;
    private final InventoryService inventoryService;
    
    public OrderController(OrderService orderService, InventoryService inventoryService) {
        this.orderService = orderService;
        this.inventoryService = inventoryService;
    }
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Map<String, Object> request) {
        String customerEmail = (String) request.get("customerEmail");
        @SuppressWarnings("unchecked")
        Map<String, Integer> items = (Map<String, Integer>) request.get("items");
        
        Order order = orderService.createOrder(customerEmail, items);
        return ResponseEntity.ok(order);
    }
    
    @PostMapping("/{orderNumber}/process")
    public ResponseEntity<String> processOrder(@PathVariable String orderNumber) {
        orderService.processOrder(orderNumber);
        return ResponseEntity.ok("Order processed successfully");
    }
    
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @GetMapping("/{orderNumber}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderNumber) {
        return ResponseEntity.ok(orderService.getOrder(orderNumber));
    }
    
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(inventoryService.getAllProducts());
    }
}
```

### 7. Configure Application Properties
Create `src/main/resources/application.properties`:

```properties
# Database configuration
spring.datasource.url=jdbc:h2:mem:ecommercedb
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
info.app.name=Advanced Transactions Lab
info.app.description=Learning Advanced Spring Transaction Management
info.app.version=5.0.0
```

### 8. Create Data Initializer
Create `src/main/java/com/example/advancedtransaction/config/DataInitializer.java`:

```java
package com.example.advancedtransaction.config;

import com.example.advancedtransaction.entity.Product;
import com.example.advancedtransaction.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final ProductRepository productRepository;
    
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            Product product1 = new Product("LAPTOP-001", "Gaming Laptop", new BigDecimal("1299.99"), 10);
            Product product2 = new Product("PHONE-001", "Smartphone", new BigDecimal("699.99"), 25);
            Product product3 = new Product("TABLET-001", "Tablet", new BigDecimal("399.99"), 15);
            Product product4 = new Product("HEADPHONES-001", "Wireless Headphones", new BigDecimal("199.99"), 30);
            
            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            
            System.out.println("Test products created successfully!");
        }
    }
}
```

### 9. Run and Test
```bash
./gradlew bootRun
```

### 10. Test Advanced Transaction Scenarios
```bash
# View available products
curl http://localhost:8080/api/orders/products

# Create an order
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "customer@example.com",
    "items": {
      "LAPTOP-001": 1,
      "PHONE-001": 2
    }
  }'

# Process the order (may fail due to simulated payment issues)
curl -X POST http://localhost:8080/api/orders/ORD-12345678/process

# View all orders to see their status
curl http://localhost:8080/api/orders

# Try creating an order with insufficient inventory
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerEmail": "customer2@example.com",
    "items": {
      "LAPTOP-001": 15
    }
  }'
```

## Verification Steps

### 1. Transaction Behavior Verification
- ✅ Confirm that successful orders update inventory and create payment records
- ✅ Verify that failed orders don't affect inventory or create payment records
- ✅ Check that notification failures don't rollback successful orders
- ✅ Test that inventory is properly released when orders fail

### 2. Expected Output Examples

**Product list response:**
```json
[
  {
    "id": 1,
    "sku": "LAPTOP-001",
    "name": "Gaming Laptop",
    "price": 1299.99,
    "stockQuantity": 9,
    "status": "ACTIVE"
  }
]
```

**Order creation response:**
```json
{
  "id": 1,
  "orderNumber": "ORD-12345678",
  "status": "PENDING",
  "totalAmount": 2699.97,
  "orderItems": [
    {
      "product": {
        "sku": "LAPTOP-001",
        "name": "Gaming Laptop"
      },
      "quantity": 1,
      "unitPrice": 1299.99
    }
  ]
}
```

### 3. Database Verification
1. Access H2 console at `http://localhost:8080/h2-console`
2. Connect with JDBC URL: `jdbc:h2:mem:ecommercedb`
3. Check that inventory is properly updated
4. Verify order status changes
5. Confirm transaction isolation

## Reflection Questions

1. **What are the different transaction propagation behaviors and when would you use each?**
   - REQUIRED: Default behavior, joins existing transaction or creates new one
   - REQUIRES_NEW: Always creates new transaction, suspends existing one
   - SUPPORTS: Joins existing transaction, doesn't create new one
   - NOT_SUPPORTED: Suspends existing transaction, executes without transaction
   - MANDATORY: Requires existing transaction, throws exception if none exists
   - NEVER: Throws exception if transaction exists

2. **How does transaction isolation affect concurrent access?**
   - READ_UNCOMMITTED: Lowest isolation, allows dirty reads
   - READ_COMMITTED: Prevents dirty reads, allows non-repeatable reads
   - REPEATABLE_READ: Prevents non-repeatable reads, allows phantom reads
   - SERIALIZABLE: Highest isolation, prevents all concurrency issues

3. **What are the trade-offs between different transaction isolation levels?**
   - Higher isolation provides better consistency but lower concurrency
   - Lower isolation provides better performance but potential consistency issues
   - Choose based on business requirements and performance needs

4. **How would you handle distributed transactions in a microservices architecture?**
   - Use Saga pattern for long-running transactions
   - Implement compensating transactions for rollbacks
   - Use event sourcing for eventual consistency
   - Consider using distributed transaction managers like Atomikos

## Key Concepts Covered

- **Transaction Propagation**: REQUIRED, REQUIRES_NEW, SUPPORTS, etc.
- **Transaction Isolation**: READ_COMMITTED, REPEATABLE_READ, SERIALIZABLE
- **Transaction Timeouts**: Setting maximum execution time
- **Rollback Rules**: Specifying which exceptions trigger rollback
- **Nested Transactions**: Understanding transaction nesting behavior
- **Resource Management**: Proper handling of database connections and locks
- **Exception Handling**: Managing partial failures in complex operations
- **Transaction Synchronization**: Coordinating multiple resources

## Resources

- [Spring Transaction Propagation](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#tx-propagation)
- [Spring Transaction Isolation](https://docs.spring.io/spring-framework/docs/current/reference/html/data-access.html#tx-isolation)
- [Baeldung: Spring Transaction Management](https://www.baeldung.com/transaction-configuration-with-jpa-and-spring)
- [Spring Boot Transaction Management](https://docs.spring.io/spring-boot/docs/current/reference/html/data.html#data.sql.transactions)

## Next Steps
After completing this lab, you should be comfortable with:
- Advanced transaction propagation scenarios
- Implementing complex transaction workflows
- Handling partial failures and rollbacks
- Understanding transaction isolation and concurrency

This prepares you for distributed transactions and microservices transaction patterns in subsequent labs. 