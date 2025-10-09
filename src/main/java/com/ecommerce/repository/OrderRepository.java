// package name here
package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// import sg.edu.nus.caproject.model.Order;
import com.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository <Order, Long> {

}
