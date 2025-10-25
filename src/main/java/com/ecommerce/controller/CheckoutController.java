package com.ecommerce.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
import com.ecommerce.service.CartService;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	
	private static final Validator paymentValidator = null;

    @Autowired
	private CartItemInterface cartItemInterface;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private PaymentService paymentService;

	@ModelAttribute("paymentForm")
	public Payment paymentForm() {
		return new Payment();
	}

	@InitBinder("paymentForm")
	private void initPaymentBinder (WebDataBinder binder) {
		binder.setValidator(paymentValidator);
	}
	
	@GetMapping("/payment-details")
	public String showPaymentDetails(Model model, HttpSession session) {
		Long customerId = (Long) session.getAttribute("customerId");
		if (customerId==null) {
			return "redirect:/login";
		}
		
		List<CartItem> cartItems = cartItemInterface.getProductsInCart(customerId);
		double totalAmount = cartItemInterface.calculateTotal(cartItems);
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
		
		Long customerId = (Long) session.getAttribute("customerId");
		if (customerId==null) {
			return "redirect:/login";
		}
		
		Optional<Customer> customerOpt = customerService.getCustomerById(customerId);
		Customer customer = customerOpt.get();
		Cart cart = cartService.findCart(customerId);
		List<CartItem> cartItems = cartItemInterface.getProductsInCart(customerId);
		double totalAmount = cartItemInterface.calculateTotal(cartItems);
		
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalAmount", totalAmount);
		
		if (cartItems.isEmpty()) {
			model.addAttribute("error", "Your cart is empty!");
			return "cart-product";
		}
		
		if(bindingResult.hasErrors()) {
			return "checkout";
		}
		
		boolean paymentSuccess = paymentService.processPayment(payment, totalAmount);
		
		if (!paymentSuccess) {
			model.addAttribute("paymentError", "Payment is unsuccessful. Please try again.");
			return "checkout";
		}
		
		Order order = orderService.createOrder(cartItems, customer);
		return "order-confirmation";
	}
}