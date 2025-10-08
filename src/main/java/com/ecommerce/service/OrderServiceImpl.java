// package sg.edu.nus.caproject.service;
package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import sg.edu.nus.caproject.model.Cart;
// import sg.edu.nus.caproject.model.CartItem;
// import sg.edu.nus.caproject.model.Customer;
// import sg.edu.nus.caproject.model.Order;
// import sg.edu.nus.caproject.model.OrderItem;
// import sg.edu.nus.caproject.repository.CartRepository;
// import sg.edu.nus.caproject.repository.OrderRepository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
// as a guide this handles creation and persistence of an order

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CartService cartService; //for reuse of total calculation
	
	@Override
	public Order createOrder(List<CartItem> cartItems, Customer customer){
		
		//create order
		Order order = new Order();
		order.setCustomer(customer);
		order.setOrderDate(LocalDateTime.now());
		
		//to store cart items as order products (for retrieval of order history)
		List<OrderItem> orderItems = cartItems.stream()
												.map(cartItem -> new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice()))
												.collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		
		//save order
		Order savedOrder = orderRepository.save(order);
		
		
		//clear cart
		//cartRepository.clearCustomerCart(customer.getId());
		return savedOrder;

	}
	
}
