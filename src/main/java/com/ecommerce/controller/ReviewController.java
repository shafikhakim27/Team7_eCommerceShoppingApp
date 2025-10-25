package com.ecommerce.controller;


import com.ecommerce.dto.ReviewForm;
import org.springframework.validation.BindingResult;
import com.ecommerce.model.Product;
import com.ecommerce.model.Customer;
import com.ecommerce.service.ShoppingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ShoppingService shoppingService;

    @GetMapping("/product/{productId}")
    public String showProductReviews(@PathVariable Long productId, Model model) {
        Product product = shoppingService.getProductById(productId);
        ReviewForm reviewForm = new ReviewForm();
        reviewForm.setProductId(productId);
        model.addAttribute("product", product);
        model.addAttribute("reviews", shoppingService.getReviewsByProductId(productId));
        model.addAttribute("reviewForm", reviewForm);
        return "review-view";
    }

    @PostMapping("/submit")
    public String submitReview(@Valid @ModelAttribute ReviewForm reviewForm, BindingResult bindingResult, Model model) {
        Product product = shoppingService.getProductById(reviewForm.getProductId());
        model.addAttribute("productId", reviewForm.getProductId());
        if (bindingResult.hasErrors()) {
            return "review-failure";
        }
        try {
        	Customer customer = shoppingService.getCustomerById(reviewForm.getCustomerId());
            shoppingService.saveReview(reviewForm.getRating(), reviewForm.getComment(), product, customer);
            return "redirect:/reviews/success";
        } catch (NoSuchElementException e) {
            return "review-failure";
        }
    }

    @GetMapping("/success")
    public String reviewSuccess() {
        return "review-success";
    }
    
}