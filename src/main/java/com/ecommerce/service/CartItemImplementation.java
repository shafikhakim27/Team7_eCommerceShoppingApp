//package name here
package com.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
// import sg.edu.nus.cart.interfacemethods.CartItemInterface;
// import sg.edu.nus.cart.model.CartItem;
// import sg.edu.nus.cart.repository.CartItemRepository;

import com.ecommerce.service.CartItemInterface;
import com.ecommerce.model.CartItem;
import com.ecommerce.repository.CartItemRepository;

//Goh Ching Tard
@Service
@Transactional
public class CartItemImplementation implements CartItemInterface {

	@Autowired
	CartItemRepository cartitemRepo;

	@Override
	@Transactional
	public boolean addToCartItem(CartItem cartitem) {
		if (cartitemRepo.save(cartitem) != null)
			return true;
		else
			return false;
	}
	
	@Override
	@Transactional
	public List<CartItem> GetProductsInCart(Long customerId) {
		return cartitemRepo.ShowItemsInCart(customerId);
	}
	
	@Override
	@Transactional
	public boolean IsItemInCart(Long productId, Long cartId, Integer quantity) {
		CartItem item = cartitemRepo.checkItemInCart(productId, cartId); 
		if (item != null) { // Is an existing cart item
			item.setQuantity(item.getQuantity()+quantity);
			return true;
		}
		else
			return false;
	}
	
	@Override
	@Transactional
	public void clearCartItems() {
		cartitemRepo.deleteAll();
	}

	//below are shirley's codes
	
	@Override
	@Transactional
	public double calculateTotal(List<CartItem> cartItems) {
		double total = 0.0;
		for (CartItem item: cartItems) {
			total += item.getQuantity()*item.getProduct().getPrice();
		}
		return total;
	}
	
}
