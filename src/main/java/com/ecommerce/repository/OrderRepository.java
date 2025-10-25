// package name here
package com.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

// import sg.edu.nus.caproject.model.Order;
import com.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {
	//return all Orders matching customerID 
	List<Order> findByCustomerId(Long customerId);

}
