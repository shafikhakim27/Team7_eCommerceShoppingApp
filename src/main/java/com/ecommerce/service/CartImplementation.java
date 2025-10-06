//package name here

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.edu.nus.cart.interfacemethods.CartInterface;
import sg.edu.nus.cart.model.Cart;
import sg.edu.nus.cart.model.Customer;
import sg.edu.nus.cart.repository.CartRepository;
import sg.edu.nus.cart.repository.CustomerRepository;

//Goh Ching Tard
@Service 
@Transactional 
public class CartImplementation implements CartInterface {

	@Autowired 
	CartRepository cartRepo; 
	
	@Autowired
	CustomerRepository customerRepo;
	
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
	
}
