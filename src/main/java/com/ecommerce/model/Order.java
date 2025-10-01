package com.ecommerce.model;

import java.util.List;
import jakarta.persistence.*;

/**
 * Order entity for the e-commerce application.
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderID;
    
    @ManyToMany
    @JoinTable(
        name = "order_products",
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> orderProducts;
    
    // Default constructor
    public Order() {}
    
    // Getters and Setters
    public Long getOrderID() {
        return orderID;
    }
    
    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }
    
    public List<Product> getOrderProducts() {
        return orderProducts;
    }
    
    public void setOrderProducts(List<Product> orderProducts) {
        this.orderProducts = orderProducts;
    }
}