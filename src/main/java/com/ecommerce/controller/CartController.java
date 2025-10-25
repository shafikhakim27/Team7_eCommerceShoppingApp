package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

import com.ecommerce.service.CartInterface;
import com.ecommerce.service.CartItemInterface;
import com.ecommerce.service.ProductInterface;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

// Goh Ching Tard
@Controller
public class CartController {
	
	@Autowired
	private ProductInterface productService;
	@Autowired
	private CartInterface cartService;
	@Autowired
	private CartItemInterface cartitemService;
	
	// To retrieve the customerID from the login session (Shafik's part)
	// For testing purpose
	Long customerId = (long) 2; // getCustomerId();

    // view items added to cart
	@GetMapping(value = "/viewcart")
	public String viewCart(Model model) {

		List<CartItem> cartItems = cartitemService.GetProductsInCart(customerId);
		double totalAmount = cartitemService.calculateTotal(cartItems);
		model.addAttribute("cartitems", cartItems);
		model.addAttribute("totalAmount", totalAmount);

		// model.addAttribute("cartitems", cartitemService.GetProductsInCart(customerId));
		// model.addAttribute("totalAmount", cartitemService.calculateTotal(cartItems));

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
