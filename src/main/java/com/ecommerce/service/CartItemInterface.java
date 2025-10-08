//package name here

import java.util.List;

import sg.edu.nus.cart.model.CartItem;

//Goh Ching Tard
public interface CartItemInterface {
	public boolean addToCartItem(CartItem cartitem);
	public List<CartItem> GetProductsInCart(Long customerId);
	public boolean IsItemInCart(Long productId, Long cartId, Integer quantity);
	public void clearCartItems();

	//added by shir
	double calculateTotal(List<CartItem> cartItems);
}


