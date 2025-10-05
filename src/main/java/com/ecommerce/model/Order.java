package com.ecommerce.model;

import java.util.List;
import java.time.LocalDateTime;
import jakarta.persistence.*;

/**
 * Order entity for the e-commerce application.
 */
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double totalAmount;
	private LocalDateTime orderDate;
    
    // Default constructor
    public Order() {}
    
    // Getters and Setters
    public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	@OneToMany (mappedBy="orders", cascade=CascadeType.ALL)
	private List<OrderItem> orderItems = new ArrayList<>();

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	// @ManyToMany
    // @JoinTable(
    //    name = "order_products",
    //    joinColumns = @JoinColumn(name = "order_id"),
    //    inverseJoinColumns = @JoinColumn(name = "product_id"))
    // private List<Product> orderProducts;
	
    // public List<Product> getOrderProducts() {
    //    return orderProducts;
    //}
    
    // public void setOrderProducts(List<Product> orderProducts) {
    //    this.orderProducts = orderProducts;
    //}

	@ManyToOne
	@JoinColumn(name="customer_id")
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
}
