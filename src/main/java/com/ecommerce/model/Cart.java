package com.ecommerce.model;

import java.util.*;

import jakarta.persistence.*;

@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Customer customer;
	
	@OneToMany (mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval=true)
	private List <CartItem> cartItems = new ArrayList<>();
	
	public Cart() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public void addItem(CartItem cartitem) {
		cartItems.add(cartitem);
		cartitem.setCart(this);
	}

	public void removeItem(CartItem cartitem) {
		cartItems.remove(cartitem);
		cartitem.setCart(null);
	}
	
	public int getTotalCartItems(Cart cart) {
		
		int count = 0;
		for (int i=0; i<cart.cartItems.size(); i++) {
			count = cart.cartItems.get(i).getQuantity() + count; 
		}
		return count;
	}
	
}
