//package name here
package com.ecommerce.service;

// import sg.edu.nus.cart.model.Cart;
import com.ecommerce.model.Cart;

//Goh Ching Tard
public interface CartInterface {
	public Cart findCart(Long customerId);
	public boolean updateCart(Cart cart);
	public void clearCart();

	//below are shirley's codes, had some conflicts with george's one so had to change
	public void clearCart(Long customerId);
}


