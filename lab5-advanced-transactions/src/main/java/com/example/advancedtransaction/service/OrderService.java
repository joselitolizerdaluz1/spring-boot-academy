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
        // TODO 9: Implement order creation with transaction management
        // 1. Generate order number
        // 2. Create order with customer
        // 3. Process each item and create order items
        // 4. Calculate total amount
        // 5. Save and return the order
        
        // TODO 10: Uncomment the following code to implement order creation:
        /*
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
        */
        
        // Placeholder return - replace with actual implementation
        throw new RuntimeException("Order creation not implemented yet");
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void processOrder(String orderNumber) {
        // TODO 11: Implement complex order processing with transaction management
        // 1. Find the order
        // 2. Reserve inventory (REQUIRES_NEW transaction)
        // 3. Process payment (REQUIRES_NEW transaction)
        // 4. Update order status
        // 5. Send notification (REQUIRES_NEW transaction)
        // 6. Handle failures and rollbacks
        
        // TODO 12: Uncomment the following code to implement order processing:
        /*
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
        */
    }
    
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        // TODO 13: Implement order list retrieval with read-only transaction
        // TODO 14: Uncomment the following code:
        /*
        return orderRepository.findAll();
        */
        
        // Placeholder return - replace with actual implementation
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public Order getOrder(String orderNumber) {
        // TODO 15: Implement order retrieval with read-only transaction
        // TODO 16: Uncomment the following code:
        /*
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));
        */
        
        // Placeholder return - replace with actual implementation
        throw new RuntimeException("Order retrieval not implemented yet");
    }
} 