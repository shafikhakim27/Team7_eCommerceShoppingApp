package com.ecommerce.model;

import java.util.List;
import jakarta.persistence.*;

/**
 * Customer entity for the e-commerce application.
 */
@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerID;
    
    @ManyToMany
    @JoinTable(
        name = "customer_products",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> customerProducts;
    
    // Default constructor
    public Customer() {}
    
    // Getters and Setters
    public Long getCustomerID() {
        return customerID;
    }
    
    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }
    
    public List<Product> getCustomerProducts() {
        return customerProducts;
    }
    
    public void setCustomerProducts(List<Product> customerProducts) {
        this.customerProducts = customerProducts;
    }
}