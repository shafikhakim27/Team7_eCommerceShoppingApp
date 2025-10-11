package com.ecommerce.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	//search by name
	@Query("Select p from OrderItem as p where p.name like CONCAT('%',:k,'%') ")
	public ArrayList<OrderItem> SearchOrderItemByName(@Param("k") String keyword);

	public List<OrderItem> findByOrderID(Long id);
}
