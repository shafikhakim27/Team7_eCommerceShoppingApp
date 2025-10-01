package com.ecommerce.model;

import java.util.List;
import jakarta.persistence.*;

/**
 * Product entity representing an e-commerce product.
 * Updated for Java 21 compatibility with modern features.
 */
@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(length = 1000)
    private String productDescription;
    
    @Column(nullable = false)
    private String category;
    
    @Column(nullable = false)
    private Double productPrice;
    
    private String productBrand;
    
    @ElementCollection
    @CollectionTable(name = "product_reviews", 
                    joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "review")
    private List<String> reviews;
    
    @ElementCollection
    @CollectionTable(name = "product_ratings", 
                    joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "rating")
    private List<Double> ratings;
    
    @ManyToMany(mappedBy = "orderProducts")
    private List<Order> orders;
    
    @ManyToMany(mappedBy = "customerProducts")
    private List<Customer> customers;
    
    // Default constructor
    public Product() {}
    
    // Constructor with essential fields
    public Product(String productName, String productDescription, String category, Double productPrice) {
        this.productName = productName;
        this.productDescription = productDescription;
        this.category = category;
        this.productPrice = productPrice;
    }
    
    // Getters and Setters with improved naming
    public Long getProductID() {
        return productID;
    }
    
    public void setProductID(Long productID) {
        this.productID = productID;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getProductDescription() {
        return productDescription;
    }
    
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Double getProductPrice() {
        return productPrice;
    }
    
    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }
    
    public String getProductBrand() {
        return productBrand;
    }
    
    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }
    
    public List<String> getReviews() {
        return reviews;
    }
    
    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }
    
    public List<Double> getRatings() {
        return ratings;
    }
    
    public void setRatings(List<Double> ratings) {
        this.ratings = ratings;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", productPrice=" + productPrice +
                ", productBrand='" + productBrand + '\'' +
                '}';
    }
}