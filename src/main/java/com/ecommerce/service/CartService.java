package com.ecommerce.service;

import com.ecommerce.model.Product;
import java.util.List;
import java.util.Map;

/**
 * CartService interface for cart operations
 */
public interface CartService {
    
    void addToCart(Long userId, Long productId, Integer quantity);
    
    void removeFromCart(Long userId, Long productId);
    
    void clearCart(Long userId);
    
    Map<String, Object> getCartItems(Long userId);
    
    Double getCartTotal(Long userId);
    
    Integer getCartItemCount(Long userId);
    
    void updateCartItemQuantity(Long userId, Long productId, Integer quantity);
}