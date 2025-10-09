// package sg.nus.iss.shoppingCart.model;

package com.ecommerce.model;

import jakarta.persistence.*;
// import lombok.AllArgsConstructor;
// import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
// @NoArgsConstructor
// @AllArgsConstructor
public class Review {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer rating; // from 1 to 5

    @Column(nullable = false, length = 1000)
    private String comment;

    // Review creation time (automatically populated)
    @CreationTimestamp
    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;

    // Many-to-one: multiple reviews correspond to one product
    @ManyToOne(fetch = FetchType.LAZY) // Lazy loading: does not actively load product details when querying reviews
    @JoinColumn(name = "product_id", nullable = false) // Foreign key
    private Product product;

    // Many-to-one: multiple reviews correspond to one user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false) // FK
    private Customer customer;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public LocalDateTime getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
}