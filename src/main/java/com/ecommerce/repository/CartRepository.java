//package name here

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository; 
import sg.edu.nus.cart.model.Cart;

//Goh Ching Tard
public interface CartRepository extends JpaRepository<Cart, Long> { 
	
	public Optional<Cart> findByCustomer_Id(Long customer_id);

}
