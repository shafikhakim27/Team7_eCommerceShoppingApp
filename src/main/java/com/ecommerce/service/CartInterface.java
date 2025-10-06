//package name here

import sg.edu.nus.cart.model.Cart;

//Goh Ching Tard
public interface CartInterface {
	public Cart findCart(Long customerId);
	public boolean updateCart(Cart cart);
	public void clearCart();
}

