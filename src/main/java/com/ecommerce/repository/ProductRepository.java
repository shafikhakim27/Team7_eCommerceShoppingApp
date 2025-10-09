package com.ecommerce.repository;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
// import sg.edu.nus.cart.model.Product;
import com.ecommerce.model.Product;

//Goh Ching Tard
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("Select p from Product p")
	public ArrayList<Product> ShowProducts();

}
