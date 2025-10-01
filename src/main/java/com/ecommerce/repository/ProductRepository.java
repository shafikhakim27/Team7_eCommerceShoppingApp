package com.ecommerce.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ecommerce.model.Product;


public interface ProductRepository extends JpaRepository<Product, Integer> {
	@Query ("SELECT DISTINCT p FROM Order o JOIN o.orderProducts p JOIN o.customer c WHERE c.userName= :userName")
	List<Product> viewOrderedProducts(@Param ("userName") String userName);
// to retrieve products in orders made by customers

}
