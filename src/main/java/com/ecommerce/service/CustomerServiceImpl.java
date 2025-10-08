//package name here

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.edu.nus.caproject.model.Customer;
import sg.edu.nus.caproject.repository.CustomerRepository;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public Optional<Customer> getCustomerById(Long id) {
		return customerRepository.findById(id);
	}
}
