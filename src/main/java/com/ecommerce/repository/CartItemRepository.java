//package name here
package com.ecommerce.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// import sg.edu.nus.cart.model.CartItem;
import com.ecommerce.model.CartItem;

//Goh Ching Tard
public interface CartItemRepository extends JpaRepository<CartItem, Long> { 

	@Query("Select c from CartItem c where c.cart.customer.id = :customerId") 
	public ArrayList<CartItem> ShowItemsInCart(@Param("customerId") Long customerId); 

	@Query("Select c from CartItem c where c.product.id = :productId and c.cart.id = :cartId") 
	public CartItem checkItemInCart(@Param("productId") Long productId, 
			@Param("cartId") Long cartId); 
}
