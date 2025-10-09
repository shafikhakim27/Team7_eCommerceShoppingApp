//package name here
package com.ecommerce.service;

import java.util.Optional;

// import sg.edu.nus.caproject.model.Customer;
import com.ecommerce.model.Customer;

public interface CustomerService {
	public Optional<Customer> getCustomerById(Long id);
}
