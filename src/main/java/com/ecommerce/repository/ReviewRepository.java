// package sg.nus.iss.shoppingCart.repository;

package com.ecommerce.repository;

// import sg.nus.iss.shoppingCart.model.Review;
import com.ecommerce.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	// Query reviews by product ID, ordered by creation time in descending order (latest reviews first)
    List<Review> findByProductIdOrderByCreatedTimeDesc(Long productId);
}