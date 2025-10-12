//package name here
package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

// import sg.edu.nus.caproject.model.Cart;
// import sg.edu.nus.caproject.model.CartItem;
// import sg.edu.nus.caproject.model.Customer;
// import sg.edu.nus.caproject.model.Order;
// import sg.edu.nus.caproject.model.Payment;
// import sg.edu.nus.caproject.repository.OrderRepository;
// import sg.edu.nus.caproject.service.CartItemService;
// import sg.edu.nus.caproject.service.CartService;
// import sg.edu.nus.caproject.service.CustomerService;
// import sg.edu.nus.caproject.service.OrderService;
// import sg.edu.nus.caproject.service.PaymentService;
// import sg.edu.nus.caproject.validator.PaymentValidator;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.Payment;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.service.CartItemImplementation;
import com.ecommerce.service.CartItemInterface;
import com.ecommerce.service.CustomerService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.OrderServiceImpl;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.PaymentServiceImpl;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	@Autowired
	private CartItemImplementation cartItemImplementation;
	
	@Autowired
	private CartItemInterface cartItemInterface;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService; //for processing/validation of payment
	
	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	// //to add Payment object to model for payment form
	// @ModelAttribute("paymentForm")
	// public Payment paymentForm() {
	// 	return new Payment();
	// }
	
	// Payment form uses @Valid annotation for validation instead of custom validator
	// @InitBinder("paymentForm") 
	// private void initPaymentBinder (WebDataBinder binder) {
	// 	binder.addValidators(paymentValidator);
	// }
	
	@GetMapping("/payment-details")
	public String showPaymentDetails(Model model, HttpSession session) {
		//for testing purpose
		Long customerId = (long) 2;
		//Long customerId = (Long) session.getAttribute("customerId");
		//if (customerId==null) {
		//	return "redirect:/login";
		//}
		
		List<CartItem> cartItems = cartItemService.getProductsInCart(customerId);
		double totalAmount = cartItemService.calculateTotal(cartItems);
		
		if (cartItems.isEmpty()) {
			model.addAttribute("error", "Your cart is empty!");
			return "cart-product";
		}
		
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalAmount", totalAmount);
		
		return "checkout";
	}
	
	@PostMapping("/checkout")
	public String processPayment(@Valid @ModelAttribute("paymentForm") Payment payment,
			BindingResult bindingResult, Model model, HttpSession session) {
		
		//for testing
		Long customerId = (long) 2;
//		Long customerId = (Long) session.getAttribute("customerId");
//		if (customerId==null) {
//			return "redirect:/login";
//		}
		
		//retrieving necessary data to generate Cart and Total
		//login is assumed to be successful (user should have been logged in at this point..?)
		Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
		Customer customer = customerOpt.get();
		
		Cart cart = cartService.findCart(customerId); //trial
		List<CartItem> cartItems = cartItemService.getProductsInCart(customerId);
		double totalAmount = cartItemService.calculateTotal(cartItems);

		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalAmount", totalAmount);
		
		if (cartItems.isEmpty()) {
			model.addAttribute("error", "Your cart is empty!");
			return "cart-product";
		}
		
		// to handle payment form validation errors
		if(bindingResult.hasErrors()) {
			return "checkout"; //back to payment form with errors shown
		}
		
		boolean paymentSuccess = paymentService.processPayment(payment, totalAmount);
		
		if (!paymentSuccess) {
			model.addAttribute("paymentError", "Payment is unsuccessful. Please try again.");
			return "checkout";
		}
		
		//create order and clear cart
		Order order = orderService.createOrder(cartItems, customer);
		cartService.clearCart(customerId);
		
		model.addAttribute("order",order);
		orderRepository.save(order);
		
		return "order-confirmation";
	}
}

