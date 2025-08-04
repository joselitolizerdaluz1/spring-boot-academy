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
        // TODO 1: Implement inventory reservation with REQUIRES_NEW propagation
        // 1. Find product with pessimistic locking
        // 2. Check if sufficient stock is available
        // 3. Update stock quantity
        // 4. Update product status if needed
        
        // TODO 2: Uncomment the following code to implement inventory reservation:
        /*
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
        */
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void releaseInventory(String sku, int quantity) {
        // TODO 3: Implement inventory release with REQUIRES_NEW propagation
        // 1. Find product with pessimistic locking
        // 2. Increase stock quantity
        // 3. Update product status if needed
        
        // TODO 4: Uncomment the following code to implement inventory release:
        /*
        Product product = productRepository.findBySkuWithLock(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
        
        product.setStockQuantity(product.getStockQuantity() + quantity);
        
        if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
            product.setStatus(Product.ProductStatus.ACTIVE);
        }
        
        productRepository.save(product);
        */
    }
    
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        // TODO 5: Implement product list retrieval with read-only transaction
        // TODO 6: Uncomment the following code:
        /*
        return productRepository.findAll();
        */
        
        // Placeholder return - replace with actual implementation
        return List.of();
    }
    
    @Transactional(readOnly = true)
    public Product getProduct(String sku) {
        // TODO 7: Implement product retrieval with read-only transaction
        // TODO 8: Uncomment the following code:
        /*
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found: " + sku));
        */
        
        // Placeholder return - replace with actual implementation
        throw new RuntimeException("Product retrieval not implemented yet");
    }
} 