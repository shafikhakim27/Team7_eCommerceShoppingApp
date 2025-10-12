package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SessionManager sessionManager;
    
    @PostMapping("/api/users/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            registeredUser.setPassword(null);
            return ResponseEntity.ok(registeredUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public String loginForm(@RequestParam String username, @RequestParam String password,
                            HttpServletRequest request, Model model) {
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            // Use SessionManager for enhanced session management
            sessionManager.login(user, request);
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @PostMapping("/api/users/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        User userOpt = userService.authenticateUser(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );
        if (userOpt != null) {
            // Use SessionManager for enhanced session management
            sessionManager.login(userOpt, request);
            userOpt.setPassword(null);
            return ResponseEntity.ok(userOpt);
        } else {
            return ResponseEntity.badRequest().body("Invalid username or password");
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class PasswordChangeRequest {
        private String email;
        private String newPassword;

        // Getters and setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    @PostMapping("/api/users/forgot-password")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@RequestBody PasswordChangeRequest request) {
        try {
            // First check if user exists
            User user = userService.findByEmail(request.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body("No account found with this email address");
            }
            
            // Change the password
            boolean success = userService.changePassword(request.getEmail(), request.getNewPassword());
            if (success) {
                return ResponseEntity.ok("Password updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to update password");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("An error occurred while updating password");
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        User currentUser = sessionManager.getCurrentUser(request);
        if (currentUser != null) {
            currentUser.setPassword(null);
            model.addAttribute("user", currentUser);
            
            // Add session info to model
            model.addAttribute("sessionInfo", sessionManager.getSessionInfo(request));
            model.addAttribute("cartItemCount", sessionManager.getCartItemCount(request));
            model.addAttribute("browseHistoryCount", sessionManager.getBrowseHistory(request).size());
            
            return "dashboard";
        }
        
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logoutUser(HttpServletRequest request) {
        sessionManager.logout(request);
        return "redirect:/login?logout=success";
    }
    
    // ============== SESSION MANAGEMENT ENDPOINTS ==============
    
    @GetMapping("/api/session/info")
    @ResponseBody
    public ResponseEntity<?> getSessionInfo(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            return ResponseEntity.ok(sessionManager.getSessionInfo(request));
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @PostMapping("/api/session/extend")
    @ResponseBody
    public ResponseEntity<?> extendSession(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            sessionManager.extendSession(request);
            return ResponseEntity.ok("Session extended successfully");
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    // ============== CART MANAGEMENT ENDPOINTS ==============
    
    @PostMapping("/api/cart/add")
    @ResponseBody
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest, HttpServletRequest request) {
        try {
            sessionManager.addToCart(request, 
                cartRequest.getProductId(), 
                cartRequest.getProductName(), 
                cartRequest.getPrice(), 
                cartRequest.getQuantity());
            return ResponseEntity.ok("Item added to cart");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    
    @GetMapping("/api/cart")
    @ResponseBody
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            return ResponseEntity.ok(sessionManager.getShoppingCart(request));
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @GetMapping("/api/cart/total")
    @ResponseBody
    public ResponseEntity<?> getCartTotal(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            return ResponseEntity.ok(sessionManager.getCartTotal(request));
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @DeleteMapping("/api/cart/clear")
    @ResponseBody
    public ResponseEntity<?> clearCart(HttpServletRequest request) {
        try {
            sessionManager.clearCart(request);
            return ResponseEntity.ok("Cart cleared");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
    
    // ============== BROWSE HISTORY ENDPOINTS ==============
    
    @PostMapping("/api/browse-history/add")
    @ResponseBody
    public ResponseEntity<?> addToBrowseHistory(@RequestBody BrowseHistoryRequest browseRequest, HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            sessionManager.addToBrowseHistory(request, 
                browseRequest.getProductId(), 
                browseRequest.getProductName(), 
                browseRequest.getCategory(), 
                browseRequest.getImageUrl());
            return ResponseEntity.ok("Added to browse history");
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @GetMapping("/api/browse-history")
    @ResponseBody
    public ResponseEntity<?> getBrowseHistory(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            return ResponseEntity.ok(sessionManager.getBrowseHistory(request));
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }
    
    @DeleteMapping("/api/browse-history/clear")
    @ResponseBody
    public ResponseEntity<?> clearBrowseHistory(HttpServletRequest request) {
        if (sessionManager.isLoggedIn(request)) {
            sessionManager.clearBrowseHistory(request);
            return ResponseEntity.ok("Browse history cleared");
        } else {
            return ResponseEntity.status(401).body("Not logged in");
        }
    }

    // ============== REQUEST CLASSES ==============
    
    public static class CartRequest {
        private Long productId;
        private String productName;
        private Double price;
        private Integer quantity;
        
        // Getters and setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
    
    public static class BrowseHistoryRequest {
        private Long productId;
        private String productName;
        private String category;
        private String imageUrl;
        
        // Getters and setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    }

}