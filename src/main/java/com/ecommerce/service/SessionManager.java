package com.ecommerce.service;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {
    
    @Autowired
    private UserService userService;
    
    // Session Keys
    private static final String USER_SESSION_KEY = "currentUser";
    private static final String USER_ID_KEY = "userId";
    private static final String CART_SESSION_KEY = "shoppingCart";
    private static final String BROWSE_HISTORY_KEY = "browseHistory";
    private static final String LAST_ACTIVITY_KEY = "lastActivity";
    private static final String SESSION_START_TIME_KEY = "sessionStartTime";
    private static final String LOGIN_TIME_KEY = "loginTime";
    
    // Session Configuration
    private static final long SESSION_TIMEOUT = 30 * 60 * 1000; // 30 minutes
    private static final int MAX_BROWSE_HISTORY = 20;
    private static final int DEFAULT_SESSION_TIMEOUT_MINUTES = 30;
    
    // Cross-session persistence (in-memory store)
    private static final Map<String, Map<String, Object>> PERSISTENT_CART_STORE = new ConcurrentHashMap<>();
    private static final Map<String, List<Map<String, Object>>> PERSISTENT_BROWSE_HISTORY = new ConcurrentHashMap<>();

    // ============== USER SESSION MANAGEMENT ==============
    
    public void login(User user, HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(USER_SESSION_KEY, user);
        session.setAttribute(USER_ID_KEY, user.getId());
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());
        session.setAttribute(SESSION_START_TIME_KEY, System.currentTimeMillis());
        session.setAttribute(LOGIN_TIME_KEY, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        initializeUserSession(session);
        restorePersistentData(user.getId(), session);
    }
    
    public User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            updateLastActivity(session);
            return (User) session.getAttribute(USER_SESSION_KEY);
        }
        return null;
    }
    
    public Long getCurrentUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            return (Long) session.getAttribute(USER_ID_KEY);
        }
        return null;
    }

    public boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long userId = getCurrentUserId(request);
            if (userId != null) {
                savePersistentData(userId, session);
            }
            session.invalidate();
        }
    }

    // ============== CART MANAGEMENT ==============
    
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
        cartItem.put("addedDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        cart.put(productId.toString(), cartItem);
        session.setAttribute(CART_SESSION_KEY, cart);
        updateLastActivity(session);
        
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            savePersistentCart(userId, cart);
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
        
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            savePersistentCart(userId, cart);
        }
    }
    
    public void clearCart(HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            throw new IllegalStateException("User must be logged in to clear cart");
        }
        
        HttpSession session = request.getSession();
        Map<String, Object> emptyCart = new HashMap<>();
        session.setAttribute(CART_SESSION_KEY, emptyCart);
        updateLastActivity(session);
        
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            savePersistentCart(userId, emptyCart);
        }
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

    // ============== BROWSE HISTORY ==============
    
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getBrowseHistory(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && isSessionValid(session)) {
            List<Map<String, Object>> history = (List<Map<String, Object>>) session.getAttribute(BROWSE_HISTORY_KEY);
            return history != null ? history : new ArrayList<>();
        }
        return new ArrayList<>();
    }
    
    public void addToBrowseHistory(HttpServletRequest request, Long productId, String productName, String category, String imageUrl) {
        if (!isLoggedIn(request)) {
            return;
        }
        
        HttpSession session = request.getSession();
        List<Map<String, Object>> history = getBrowseHistory(request);
        
        Map<String, Object> historyItem = new HashMap<>();
        historyItem.put("productId", productId);
        historyItem.put("productName", productName);
        historyItem.put("category", category);
        historyItem.put("imageUrl", imageUrl);
        historyItem.put("viewedAt", System.currentTimeMillis());
        historyItem.put("viewedDate", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        history.removeIf(item -> productId.equals(item.get("productId")));
        history.add(0, historyItem);
        
        if (history.size() > MAX_BROWSE_HISTORY) {
            history = history.subList(0, MAX_BROWSE_HISTORY);
        }
        
        session.setAttribute(BROWSE_HISTORY_KEY, history);
        updateLastActivity(session);
        
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            savePersistentBrowseHistory(userId, history);
        }
    }
    
    public void clearBrowseHistory(HttpServletRequest request) {
        if (!isLoggedIn(request)) {
            return;
        }
        
        HttpSession session = request.getSession();
        List<Map<String, Object>> emptyHistory = new ArrayList<>();
        session.setAttribute(BROWSE_HISTORY_KEY, emptyHistory);
        updateLastActivity(session);
        
        Long userId = getCurrentUserId(request);
        if (userId != null) {
            savePersistentBrowseHistory(userId, emptyHistory);
        }
    }

    // ============== SESSION VALIDATION ==============
    
    public boolean isSessionValid(HttpSession session) {
        if (session == null) {
            return false;
        }
        
        Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_KEY);
        if (lastActivity == null) {
            return false;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastActivity > SESSION_TIMEOUT) {
            return false;
        }
        
        Object user = session.getAttribute(USER_SESSION_KEY);
        return user != null;
    }
    
    public long getSessionTimeRemaining(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Long lastActivity = (Long) session.getAttribute(LAST_ACTIVITY_KEY);
            if (lastActivity != null) {
                long elapsed = System.currentTimeMillis() - lastActivity;
                return Math.max(0, SESSION_TIMEOUT - elapsed);
            }
        }
        return 0;
    }
    
    public Map<String, Object> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> sessionInfo = new HashMap<>();
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            sessionInfo.put("sessionId", session.getId());
            sessionInfo.put("creationTime", session.getCreationTime());
            sessionInfo.put("lastAccessedTime", session.getLastAccessedTime());
            sessionInfo.put("maxInactiveInterval", session.getMaxInactiveInterval());
            sessionInfo.put("isNew", session.isNew());
            sessionInfo.put("timeRemaining", getSessionTimeRemaining(request));
            sessionInfo.put("cartItemCount", getCartItemCount(request));
            sessionInfo.put("browseHistoryCount", getBrowseHistory(request).size());
            
            String loginTime = (String) session.getAttribute(LOGIN_TIME_KEY);
            sessionInfo.put("loginTime", loginTime);
            
            Long sessionStart = (Long) session.getAttribute(SESSION_START_TIME_KEY);
            if (sessionStart != null) {
                long sessionDuration = System.currentTimeMillis() - sessionStart;
                sessionInfo.put("sessionDuration", sessionDuration);
            }
        }
        
        return sessionInfo;
    }

    // ============== SESSION TIMEOUT MANAGEMENT ==============
    
    public void extendSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            updateLastActivity(session);
            session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT_MINUTES * 60);
        }
    }
    
    // ============== PRIVATE HELPER METHODS ==============
    
    private void savePersistentData(Long userId, HttpSession session) {
        if (userId != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> cart = (Map<String, Object>) session.getAttribute(CART_SESSION_KEY);
            if (cart != null) {
                savePersistentCart(userId, cart);
            }
            
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> history = (List<Map<String, Object>>) session.getAttribute(BROWSE_HISTORY_KEY);
            if (history != null) {
                savePersistentBrowseHistory(userId, history);
            }
        }
    }
    
    private void restorePersistentData(Long userId, HttpSession session) {
        if (userId != null) {
            Map<String, Object> persistentCart = PERSISTENT_CART_STORE.get(userId.toString());
            if (persistentCart != null) {
                session.setAttribute(CART_SESSION_KEY, new HashMap<>(persistentCart));
            }
            
            List<Map<String, Object>> persistentHistory = PERSISTENT_BROWSE_HISTORY.get(userId.toString());
            if (persistentHistory != null) {
                session.setAttribute(BROWSE_HISTORY_KEY, new ArrayList<>(persistentHistory));
            }
        }
    }
    
    private void savePersistentCart(Long userId, Map<String, Object> cart) {
        PERSISTENT_CART_STORE.put(userId.toString(), new HashMap<>(cart));
    }
    
    private void savePersistentBrowseHistory(Long userId, List<Map<String, Object>> history) {
        PERSISTENT_BROWSE_HISTORY.put(userId.toString(), new ArrayList<>(history));
    }
    
    private void initializeUserSession(HttpSession session) {
        if (session.getAttribute(CART_SESSION_KEY) == null) {
            session.setAttribute(CART_SESSION_KEY, new HashMap<String, Object>());
        }
        if (session.getAttribute(BROWSE_HISTORY_KEY) == null) {
            session.setAttribute(BROWSE_HISTORY_KEY, new ArrayList<Map<String, Object>>());
        }
        session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT_MINUTES * 60);
    }
    
    private void updateLastActivity(HttpSession session) {
        session.setAttribute(LAST_ACTIVITY_KEY, System.currentTimeMillis());
    }
}
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

