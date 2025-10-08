package com.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class CartItem {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int quantity;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	//only to fetch CartItem and not to load Cart from database immediately whenever 1 cart item is loaded
	@JoinColumn(name="cart_id", nullable=false) //foreign key in Cart table
	//nullable = false ensures that cart_id column is NOT NULL
	private Cart cart;
	
	@ManyToOne
	@JoinColumn(name="product_id") //foreign key in CartItem table
	private Product product;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	
	
}
