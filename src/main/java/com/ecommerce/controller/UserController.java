package com.ecommerce.controller;

import com.ecommerce.model.User;
import com.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    
    @Autowired
    private UserService userService;
    
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
                            HttpSession session, Model model) {
        User user = userService.authenticateUser(username, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    @PostMapping("/api/users/login")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
        User userOpt = userService.authenticateUser(
            loginRequest.getUsername(),
            loginRequest.getPassword()
        );
        if (userOpt != null) {
            session.setAttribute("userId", userOpt.getId());
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
    public String dashboardPage(Model model, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId != null) {
            User sessionUser = userService.findById(userId);
            if (sessionUser != null) {
                sessionUser.setPassword(null);
                model.addAttribute("user", sessionUser);
                return "dashboard";
            }
        }
        
        return "redirect:/login";
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @PostMapping("/logout")
    public String logoutUser(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=success";
    }

}