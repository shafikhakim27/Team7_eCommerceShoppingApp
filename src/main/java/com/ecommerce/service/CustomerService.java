//package name here

import java.util.Optional;

import sg.edu.nus.caproject.model.Customer;

public interface CustomerService {
	public Optional<Customer> getCustomerById(Long id);
}
