package com.ecommerce.model;

import java.util.List;
import jakarta.persistence.*;

/**
 * Customer entity for the e-commerce application.
 */
@Entity
@Table(name = "customer")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userPw;
    
    @ManyToMany
    @JoinTable(
        name = "customer_products",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> customerProducts;

	@OneToMany(mappedBy="customer")
	private List<Order> orders;
	
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	@OneToOne(mappedBy="customer")
	private Cart cart;
    
    // Default constructor
    public Customer() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
		this.id = id;
	}

    public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
    
    public List<Product> getCustomerProducts() {
        return customerProducts;
    }
    
    public void setCustomerProducts(List<Product> customerProducts) {
        this.customerProducts = customerProducts;
    }

	public Cart getCart() {
		return cart;
	}
	
	public void setCart(Cart cart) {
		this.cart = cart;
	}
}
