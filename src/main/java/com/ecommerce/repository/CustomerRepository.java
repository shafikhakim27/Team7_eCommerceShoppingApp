//package name here

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.cart.model.Customer;

//Goh Ching Tard
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
