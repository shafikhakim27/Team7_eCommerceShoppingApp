package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import com.ecommerce.service.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UserController - Optimized controller for User operations
 * Lean baseline development with proper error handling and REST endpoints
 */
@Controller
@RequestMapping
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private SessionManager sessionManager;
    
    // ===== WEB PAGE ENDPOINTS =====
    
    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }
    
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {
        return "forgot-password";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(HttpServletRequest request, Model model) {
        User currentUser = sessionManager.getCurrentUser(request);
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", currentUser);
        model.addAttribute("sessionInfo", sessionManager.getSessionInfo(request));
        return "dashboard";
    }
    
    // ===== FORM PROCESSING ENDPOINTS =====
    
    @PostMapping("/login")
    public String processLogin(@RequestParam String username, 
                              @RequestParam String password,
                              HttpServletRequest request, 
                              Model model,
                              RedirectAttributes redirectAttributes) {
        try {
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Username is required");
                return "login";
            }
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Password is required");
                return "login";
            }
            
            User user = userService.authenticateUser(username.trim(), password);
            if (user != null) {
                sessionManager.login(user, request);
                redirectAttributes.addFlashAttribute("success", "Welcome back, " + user.getUsername() + "!");
                return "redirect:/dashboard";
            } else {
                model.addAttribute("error", "Invalid username or password");
                model.addAttribute("username", username);
                return "login";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Login failed. Please try again.");
            return "login";
        }
    }
    
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute User user,
                                    BindingResult bindingResult,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                return "register";
            }
            
            User registeredUser = userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "register";
        }
    }
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                      @RequestParam String newPassword,
                                      Model model,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("error", "Email is required");
                return "forgot-password";
            }
            if (newPassword == null || newPassword.length() < 6) {
                model.addAttribute("error", "Password must be at least 6 characters");
                return "forgot-password";
            }
            
            boolean success = userService.changePassword(email.trim(), newPassword);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Password updated successfully! Please login with your new password.");
                return "redirect:/login";
            } else {
                model.addAttribute("error", "No account found with this email address");
                model.addAttribute("email", email);
                return "forgot-password";
            }
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        sessionManager.logout(request);
        redirectAttributes.addFlashAttribute("success", "You have been logged out successfully");
        return "redirect:/login";
    }

    @PostMapping("/change-password")
    @ResponseBody
    public ResponseEntity<String> changePassword(@RequestParam String currentPassword,
                                                  @RequestParam String newPassword,
                                                  @RequestParam String confirmPassword,
                                                  HttpServletRequest request) {
        try {
            if (!newPassword.equals(confirmPassword)) {
                return ResponseEntity.badRequest().body("New passwords do not match");
            }
            
            User currentUser = sessionManager.getCurrentUser(request);
            if (currentUser == null) {
                return ResponseEntity.badRequest().body("User not logged in");
            }
            
            boolean success = userService.changePassword(currentUser.getEmail(), newPassword);
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