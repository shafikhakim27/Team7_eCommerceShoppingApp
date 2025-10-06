package sg.edu.nus.cart.interfacemethods;

import sg.edu.nus.cart.model.Cart;

//Goh Ching Tard
public interface CartInterface {
	public Cart findCart(Long customerId);
	public boolean updateCart(Cart cart);
	public void clearCart();
}
