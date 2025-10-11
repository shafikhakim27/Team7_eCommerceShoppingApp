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
    
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000;


    public void login(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, user.getUsername());
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());

        initializeUserSession(session);
    }
    
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            updateLastActivity(session);
            return (User) session.getAttribute(USER_SESSION_KEY);
        }
        return null;
    }

    public boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

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
    
    public void addToCart(HttpServletRequest request, Long productId, String productName, Double price, Integer quantity) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to add items to cart");
        }
        
        HttpSession session = request.getSession();
        Map<String, Object> cart = getShoppingCart(request);
        

        Map<String, Object> cartItem = new HashMap<>();
        cartItem.put("productId", productId);
        cartItem.put("productName", productName);
        cartItem.put("price", price);
        cartItem.put("quantity", quantity);
        cartItem.put("subtotal", price * quantity);
        cartItem.put("addedAt", System.currentTimeMillis());
        

        cart.put(productId.toString(), cartItem);
        session.setAttribute(CART_SESSION_KEY, cart);
        updateLastActivity(session);
    }
    
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
    
    public void clearCart(HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to clear cart");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(CART_SESSION_KEY, new HashMap<String, Object>());
        updateLastActivity(session);
    }

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


    public void saveCheckoutData(HttpServletRequest request, Map<String, Object> checkoutData) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in for checkout");
        }
        
        HttpSession session = request.getSession();
        session.setAttribute(CHECKOUT_DATA_KEY, checkoutData);
        updateLastActivity(session);
    }
    

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
    

    public void clearCheckoutData(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(CHECKOUT_DATA_KEY);
            updateLastActivity(session);
        }
    }


    public void saveUserPreference(HttpServletRequest request, String key, Object value) {
        if (!isLoggedIn(request)) {
            return;
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
    
    public void addToPurchaseHistory(HttpServletRequest request, Long productId, String productName) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> purchaseHistory = (List<Map<String, Object>>) getUserPreference(request, "purchaseHistory");
        if (purchaseHistory == null) {
            purchaseHistory = new ArrayList<>();
        }
        
        purchaseHistory.removeIf(item -> item.get("productId").equals(productId));

        Map<String, Object> historyItem = new HashMap<>();
        historyItem.put("productId", productId);
        historyItem.put("productName", productName);
        historyItem.put("viewedAt", System.currentTimeMillis());
        
        purchaseHistory.add(0, historyItem);
        
        if (purchaseHistory.size() > 20) {
            purchaseHistory = purchaseHistory.subList(0, 20);
        }

        saveUserPreference(request, "purchaseHistory", purchaseHistory);
    }
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getPurchaseHistory(HttpServletRequest request) {
        List<Map<String, Object>> history = (List<Map<String, Object>>) getUserPreference(request, "purchaseHistory");
        return history != null ? history : new ArrayList<>();
    }


    private void initializeUserSession(HttpSession session) {
        if (session.getAttribute(CART_SESSION_KEY) == null) {
            session.setAttribute(CART_SESSION_KEY, new HashMap<String, Object>());
        }
        if (session.getAttribute(USER_PREFERENCES_KEY) == null) {
            session.setAttribute(USER_PREFERENCES_KEY, new HashMap<String, Object>());
        }
    }
    
    private boolean isSessionValid(HttpSession session) {
        Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_KEY);
        if (lastActivity == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastActivity) < SESSION_TIMEOUT;
    }
    
    private void updateLastActivity(HttpSession session) {
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());
    }

    public Map<String, Object> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> info = new HashMap<>();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            info.put("sessionId", session.getId());
            info.put("isLoggedIn", isLoggedIn(request));
            info.put("cartItemCount", getCartItemCount(request));
            info.put("cartTotal", getCartTotal(request));
            info.put("purchaseHistorySize", getPurchaseHistory(request).size());
            
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

