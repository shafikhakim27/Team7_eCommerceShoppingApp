//package name here
package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

// import sg.edu.nus.cart.model.Customer;
import com.ecommerce.model.Customer;


//Goh Ching Tard
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
