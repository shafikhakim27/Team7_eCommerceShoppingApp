// package name here
package com.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// import sg.edu.nus.caproject.model.Order;
import com.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {
	//auto-generate SQL, return customerID匹配的所有Order
	List<Order> findByCustomerId(Long customerId);

}
