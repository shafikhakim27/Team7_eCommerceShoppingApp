//package name here

import org.springframework.beans.factory.annotation.Autowired;       
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import sg.edu.nus.cart.interfacemethods.CartInterface;
import sg.edu.nus.cart.interfacemethods.CartItemInterface;
import sg.edu.nus.cart.interfacemethods.ProductInterface;

import sg.edu.nus.cart.service.ProductImplementation; 

//Goh Ching Tard
@Controller 
public class ProductController { 

	@Autowired 
	private ProductInterface productService;
	@Autowired 
	private CartInterface cartService;
	@Autowired 
	private CartItemInterface cartitemService;
	
	@Autowired 
	public void setProductService(ProductImplementation productServiceImplementation) { 
		this.productService = productServiceImplementation; 
	} 
	
	// For testing purpose
	@GetMapping("/products") 	
	public String clearAllCartItems(Model model) { 
		cartitemService.clearCartItems();
		cartService.clearCart();
		return "redirect:/products/list";
	}
	
	@GetMapping("/products/list") 	
	public String showProductPage(Model model) { 

		model.addAttribute("products", productService.retrieveProducts()); 

		return "displayProducts"; 
	} 
}

