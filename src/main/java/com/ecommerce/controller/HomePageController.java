package com.ecommerce.controller;

import com.ecommerce.service.ProductInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomePageController {

    @Autowired
    private ProductInterface productService;

    @GetMapping({"", "/"})
    public String homePage(Model model) {
        model.addAttribute("pageTitle", "FreshMart");
        model.addAttribute("heroTitle", "Fresh Groceries Delivered to Your Door");
        model.addAttribute("heroDescription", "Shop from our wide selection of fresh produce, dairy, bakery items, and more. Same-day delivery available.");
        model.addAttribute("featuredProducts", productService.getFeaturedProducts(Integer.MAX_VALUE));
        return "home";
    }
}
