//package name here

import java.util.List;

import org.springframework.stereotype.Service;

import sg.edu.nus.caproject.model.CartItem;
import sg.edu.nus.caproject.model.Customer;
import sg.edu.nus.caproject.model.Order;

@Service
public interface OrderService {
	// as a guide this should handle creation and persistence of an order
	Order createOrder(List<CartItem> cartItems, Customer customer);

}
