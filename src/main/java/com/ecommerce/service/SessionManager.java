package com.ecommerce.service;

import org.springframework.stereotype.Component;
import com.ecommerce.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SessionManager {
    
    private static final String USER_SESSION_KEY = "currentUser";
    private static final String CART_SESSION_KEY = "shoppingCart";
    private static final String CHECKOUT_DATA_KEY = "checkoutData";
    private static final String LAST_ACTIVITY_KEY = "lastActivity";
    private static final String USER_PREFERENCES_KEY = "userPreferences";
    
    // Session timeout in milliseconds (30 minutes)
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;

    // ========== AUTHENTICATION METHODS ==========
    
    // login and create user session
    public void login(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, user);
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());
        
        // Initialize user-specific session data
        initializeUserSession(session);
    }
    
    // get current user
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            updateLastActivity(session);
            return (User) session.getAttribute(USER_SESSION_KEY);
        }
        return null;
    }

    // check if user is logged in
    public boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    // logout and invalidate session
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    // ========== CART MANAGEMENT METHODS ==========
    
    // Get user's shopping cart from session
    @SuppressWarnings("unchecked")
    public Map<String, Object> getShoppingCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            updateLastActivity(session);
            Map<String, Object> cart = (Map<String, Object>) session.getAttribute(CART_SESSION_KEY);
            return cart != null ? cart : new HashMap<>();
        }
        return new HashMap<>();
    }
    
    // Add item to cart
    public void addToCart(HttpServletRequest request, Long productId, String productName, Double price, Integer quantity) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to add items to cart");
        }
        
        HttpSession session = request.getSession();
        Map<String, Object> cart = getShoppingCart(request);
        
        // Create cart item
        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("productId", productId);
        cartItem.put("productName", productName);
        cartItem.put("price", price);
        cartItem.put("quantity", quantity);
        cartItem.put("subtotal", price * quantity);
        cartItem.put("addedAt", System.currentTimeMillis());
        
        // Add to cart (use productId as key)
        cart.put(productId.toString(), cartItem);
        session.setAttribute(CART_SESSION_KEY, cart);
        updateLastActivity(session);
    }
    
    // Update cart item quantity
    public void updateCartItemQuantity(HttpServletRequest request, Long productId, Integer newQuantity) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to update cart");
        }
        
        Map<String, Object> cart = getShoppingCart(request);
        String productKey = productId.toString();
        
        if (cart.containsKey(productKey)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> cartItem = (Map<String, Object>) cart.get(productKey);
            Double price = (Double) cartItem.get("price");
            
            cartItem.put("quantity", newQuantity);
            cartItem.put("subtotal", price * newQuantity);
            
            HttpSession session = request.getSession();
            session.setAttribute(CART_SESSION_KEY, cart);
            updateLastActivity(session);
        }
    }
    
    // Remove item from cart
    public void removeFromCart(HttpServletRequest request, Long productId) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to modify cart");
        }
        
        Map<String, Object> cart = getShoppingCart(request);
        cart.remove(productId.toString());
        
        HttpSession session = request.getSession();
        session.setAttribute(CART_SESSION_KEY, cart);
        updateLastActivity(session);
    }
    
    // Clear entire cart
    public void clearCart(HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to clear cart");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(CART_SESSION_KEY, new HashMap<String, Object>());
        updateLastActivity(session);
    }
    
    // Get cart total
    public Double getCartTotal(HttpServletRequest request) {
        Map<String, Object> cart = getShoppingCart(request);
        return cart.values().stream()
                .mapToDouble(item -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> cartItem = (Map<String, Object>) item;
                    return (Double) cartItem.get("subtotal");
                })
                .sum();
    }
    
    // Get cart item count
    public Integer getCartItemCount(HttpServletRequest request) {
        Map<String, Object> cart = getShoppingCart(request);
        return cart.values().stream()
                .mapToInt(item -> {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> cartItem = (Map<String, Object>) item;
                    return (Integer) cartItem.get("quantity");
                })
                .sum();
    }

    // ========== CHECKOUT METHODS ==========
    
    // Save checkout data to session
    public void saveCheckoutData(HttpServletRequest request, Map<String, Object> checkoutData) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in for checkout");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(CHECKOUT_DATA_KEY, checkoutData);
        updateLastActivity(session);
    }
    
    // Get checkout data from session
    @SuppressWarnings("unchecked")
    public Map<String, Object> getCheckoutData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            updateLastActivity(session);
            Map<String, Object> checkoutData = (Map<String, Object>) session.getAttribute(CHECKOUT_DATA_KEY);
            return checkoutData != null ? checkoutData : new HashMap<>();
        }
        return new HashMap<>();
    }
    
    // Clear checkout data after successful order
    public void clearCheckoutData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(CHECKOUT_DATA_KEY);
            updateLastActivity(session);
        }
    }

    // ========== USER PREFERENCES METHODS ==========
    
    // Save user preferences (for purchase history, favorites, etc.)
    public void saveUserPreference(HttpServletRequest request, String key, Object value) {
        if (!isLoggedIn(request)) {
            return; // Silent fail for preferences
        }
        
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        Map<String, Object> preferences = (Map<String, Object>) session.getAttribute(USER_PREFERENCES_KEY);
        if (preferences == null) {
            preferences = new HashMap<>();
        }
        
        preferences.put(key, value);
        session.setAttribute(USER_PREFERENCES_KEY, preferences);
        updateLastActivity(session);
    }
    
    // Get user preference
    public Object getUserPreference(HttpServletRequest request, String key) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> preferences = (Map<String, Object>) session.getAttribute(USER_PREFERENCES_KEY);
            if (preferences != null) {
                updateLastActivity(session);
                return preferences.get(key);
            }
        }
        return null;
    }
    
    // Add to purchase history
    public void addToPurchaseHistory(HttpServletRequest request, Long productId, String productName) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> purchaseHistory = (List<Map<String, Object>>) getUserPreference(request, "purchaseHistory");
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        
        // Remove if already exists to avoid duplicates
        purchaseHistory.removeIf(item -> item.get("productId").equals(productId));

        // Add to beginning of list
        Map<String, Object> historyItem = new HashMap<>();
        historyItem.put("productId", productId);
        historyItem.put("productName", productName);
        historyItem.put("viewedAt", System.currentTimeMillis());
        
        browseHistory.add(0, historyItem);
        
        // Keep only last 20 items
        if (browseHistory.size() > 20) {
            browseHistory = browseHistory.subList(0, 20);
        }
        
        saveUserPreference(request, "purchaseHistory", browseHistory);
    }
    
    // Get purchase history
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPurchaseHistory(HttpServletRequest request) {
        List<Map<String, Object>> history = (List<Map<String, Object>>) getUserPreference(request, "purchaseHistory");
        return history != null ? history : new ArrayList<>();
    }

    // ========== UTILITY METHODS ==========
    
    // Initialize session data for new user
    private void initializeUserSession(HttpSession session) {
        if (session.getAttribute(CART_SESSION_KEY) == null) {
            session.setAttribute(CART_SESSION_KEY, new HashMap<String, Object>());
        }
        if (session.getAttribute(USER_PREFERENCES_KEY) == null) {
            session.setAttribute(USER_PREFERENCES_KEY, new HashMap<String, Object>());
        }
    }
    
    // Check if session is still valid (not timed out)
    private boolean isSessionValid(HttpSession session) {
        Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_KEY);
        if (lastActivity == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastActivity) < SESSION_TIMEOUT;
    }
    
    // Update last activity timestamp
    private void updateLastActivity(HttpSession session) {
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());
    }
    
    // Get session info for debugging
    public Map<String, Object> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            info.put("sessionId", session.getId());
            info.put("isLoggedIn", isLoggedIn(request));
            info.put("cartItemCount", getCartItemCount(request));
            info.put("cartTotal", getCartTotal(request));
            info.put("browseHistorySize", getBrowseHistory(request).size());
            
            Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_KEY);
            if (lastActivity != null) {
                info.put("lastActivity", lastActivity);
                info.put("sessionValid", isSessionValid(session));
            }
        } else {
            info.put("sessionExists", false);
        }
        
        return info;
    }
}

