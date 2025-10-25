package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.ProductInterface;
import com.ecommerce.service.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductInterface productService;
    
    @Autowired
    private SessionManager sessionManager;

    @GetMapping({"", "/", "/list"})
    public String showProductsPage(Model model,
                                @RequestParam(required = false) String category,
                                @RequestParam(required = false) String search,
                                HttpServletRequest request) {
        try {
            List<Product> products;
            
            if (search != null && !search.trim().isEmpty()) {
                products = productService.findByNameContainingIgnoreCase(search.trim());
                model.addAttribute("searchTerm", search.trim());
            } else if (category != null && !category.trim().isEmpty()) {
                products = productService.findByCategory(category.trim());
                model.addAttribute("selectedCategory", category.trim());
            } else {
                products = productService.getAllProducts();
            }
            
            model.addAttribute("products", products);
            model.addAttribute("productCount", products.size());
            
            if (sessionManager.isLoggedIn(request)) {
                model.addAttribute("currentUser", sessionManager.getCurrentUser(request));
            }
            
            return "displayProducts";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load products. Please try again.");
            return "error";
        }
    }
    
    @GetMapping("/{id}")
    public String showProductDetails(@PathVariable Long id, 
                                   Model model, 
                                   HttpServletRequest request) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                model.addAttribute("error", "Product not found");
                return "error";
            }
            
            model.addAttribute("product", product);
            
            List<Product> relatedProducts = productService.findByCategory(product.getCategory())
                .stream()
                .filter(p -> !p.getId().equals(id))
                .limit(4)
                .toList();
            model.addAttribute("relatedProducts", relatedProducts);
            
            if (sessionManager.isLoggedIn(request)) {
                model.addAttribute("currentUser", sessionManager.getCurrentUser(request));
                
                sessionManager.addToBrowseHistory(request, product.getId(), 
                    product.getName(), product.getCategory(), 
                    product.getImageUrl() != null ? product.getImageUrl() : "");
            }
            
            return "productDetails";
        } catch (Exception e) {
            model.addAttribute("error", "Unable to load product details. Please try again.");
            return "error";
        }
    }

    @GetMapping("/api/all")
    @ResponseBody
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        try {
            Product product = productService.getProductById(id);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/api/categories")
    @ResponseBody
    public ResponseEntity<List<String>> getCategories() {
        try {
            List<String> categories = List.of("Electronics", "Clothing", "Books", "Home", "Sports");
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/api/search")
    @ResponseBody
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String q) {
        try {
            if (q == null || q.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            List<Product> products = productService.findByNameContainingIgnoreCase(q.trim());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/api/category/{category}")
    @ResponseBody
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        try {
            List<Product> products = productService.findByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
