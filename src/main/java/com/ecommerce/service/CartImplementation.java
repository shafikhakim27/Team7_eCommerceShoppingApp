//package name here
package com.ecommerce.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
// import sg.edu.nus.cart.interfacemethods.CartInterface;
// import sg.edu.nus.cart.model.Cart;
// import sg.edu.nus.cart.model.Customer;
// import sg.edu.nus.cart.repository.CartRepository;
// import sg.edu.nus.cart.repository.CustomerRepository;

import com.ecommerce.service.CartInterface;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Customer;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CustomerRepository;


//Goh Ching Tard
@Service
@Transactional
public class CartImplementation implements CartInterface {

	@Autowired
	CartRepository cartRepo;
	
	@Autowired
	CustomerRepository customerRepo;

	//below 2 autowired added by shir
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	CustomerInterface customerService;
	
	@Override
	@Transactional
	public Cart findCart(Long customerId) {
		
	    Cart customerCart = cartRepo.findByCustomer_Id(customerId).orElse(null);
	        if (customerCart == null) {
	        	Cart newCart = new Cart();
	    	    Customer customer = customerRepo.findById(customerId).orElse(null);
	    		newCart.setCustomer(customer);
	        	return newCart;
	        }
	        return customerCart;
	}

	/*shir's code for findCart
	@Override
	@Transactional
	public Cart findCart(Long customerId) {
		Optional<Cart> cartOpt = cartRepository.findByCustomerId(customerId);
		if(cartOpt.isPresent()) {
			return cartOpt.get();
		} else {
			Cart newCart = new Cart();
			Customer customer = customerService.getCustomerById(customerId).orElse(null);
			newCart.setCustomer(customer);
			
			Cart savedCart = cartRepository.save(newCart); // to persist cart! 
			return savedCart;
		}
		*/
	
	@Override
	@Transactional
	public boolean updateCart(Cart cart) {
		if (cartRepo.save(cart) != null)
			return true;
		else
			return false;
	}

	@Override
	@Transactional
	public void clearCart() {
		cartRepo.deleteAll();
	}

	/* shir's code
	@Override
	@Transactional
	public void clearCart(Long customerId) {
		Optional<Cart> cartOpt = cartRepository.findByCustomerId(customerId);
		if (cartOpt.isPresent()) {
			Cart cart = cartOpt.get();
			// Long cartId = cart.getId();
			
			cart.getCartItems().clear();
			cartRepository.save(cart); // "update status of cart as empty"
		}
	}
	*/
	
}
