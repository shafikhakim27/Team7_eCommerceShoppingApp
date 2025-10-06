//package name here

import org.springframework.beans.factory.annotation.Autowired;     
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.nus.cart.interfacemethods.CartInterface;
import sg.edu.nus.cart.interfacemethods.CartItemInterface;
import sg.edu.nus.cart.interfacemethods.ProductInterface;
import sg.edu.nus.cart.model.Cart;
import sg.edu.nus.cart.model.CartItem;
import sg.edu.nus.cart.model.Product;

// Goh Ching Tard
@Controller 
public class CartController {
	
	@Autowired 
	private ProductInterface productService;
	@Autowired 
	private CartInterface cartService;
	@Autowired 
	private CartItemInterface cartitemService;
	
	// To retrieve the customerID from the login session (Shafiq's part)
	// For testing purpose
	Long customerId = (long) 2; // getCustomerId();

    // view items added to cart  
	@GetMapping(value = "/viewcart") 
	public String viewCart(Model model) { 

		model.addAttribute("cartitems", cartitemService.GetProductsInCart(customerId)); 

		return "CartProducts"; 
	} 

    // save product selected to cart  
	@GetMapping(value = "/addtocart") 
	public String addItemToCart(@RequestParam Long productId, @RequestParam Integer quantity, Model model) {

		Product productSelected = productService.findProductById(productId);

		Cart cart = cartService.findCart(customerId);
	   	
		if (!cartitemService.IsItemInCart(productId, cart.getId(), quantity)) {

			CartItem cartitem = new CartItem();
			cartitem.setQuantity(quantity);
			cartitem.setProduct(productSelected);
			
			cartitemService.addToCartItem(cartitem); // save item to db for CartItem entity			
			cart.addItem(cartitem); // add new item to cart entity
		}
        
		// save new item in Cart entity or new quantity for an existing item to db
	    cartService.updateCart(cart); 
	   
	    model.addAttribute("cartItemsQuantity", cart.getTotalCartItems(cart));
        return "forward:/products/list";         
    }

}
