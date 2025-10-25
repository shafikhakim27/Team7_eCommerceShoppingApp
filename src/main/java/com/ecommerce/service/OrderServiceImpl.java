package com.ecommerce.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.model.CartItem;
import com.ecommerce.model.User;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.repository.OrderRepository;
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
	public Order createOrder(List<CartItem> cartItems, User user){
		
		//create order
		Order order = new Order();
		order.setUser(user);
		order.setOrderDate(LocalDateTime.now());
		
		//to store cart items as order products (for retrieval of order history)
		List<OrderItem> orderItems = cartItems.stream()
												.map(cartItem -> new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity(), cartItem.getProduct().getPrice().doubleValue()))
												.collect(Collectors.toList());
		
		order.setOrderItems(orderItems);
		
		//save order
		Order savedOrder = orderRepository.save(order);
		
		
		//clear cart
		//cartRepository.clearCustomerCart(customer.getId());
		return savedOrder;

	}
	
}
