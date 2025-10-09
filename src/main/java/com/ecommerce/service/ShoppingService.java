// package sg.nus.iss.shoppingCart.service;

package com.ecommerce.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
// import sg.nus.iss.shoppingCart.model.Product;
// import sg.nus.iss.shoppingCart.model.Review;
// import sg.nus.iss.shoppingCart.model.Customer;
// import sg.nus.iss.shoppingCart.repository.ProductRepository;
// import sg.nus.iss.shoppingCart.repository.ReviewRepository;
// import sg.nus.iss.shoppingCart.repository.CustomerRepository;
// import sg.nus.iss.shoppingCart.interfacemethods.ProductInterface;

import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.Customer;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.repository.ReviewRepository;
import com.ecommerce.repository.CustomerRepository;
import com.ecommerce.service.ProductInterface;

@Service
@Transactional
public class ShoppingService implements ProductInterface  {
	
	@Autowired
    private ProductRepository productRepository;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private ReviewRepository reviewRepository;
	
    /** Product Service */
    public Product getProductById(Long productId) {
    	// Call Repository to query, throw exception if no result
        return productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product does not exist, product ID：" + productId));
    }
    
    @Override
    @Transactional
    public List<Product> SearchProductByName(String name) {
        return productRepository.searchProductByName(name);
    }

    @Override
    @Transactional
    public List<Product> SearchProductByCategory(String category) {
        return productRepository.searchProductByCategory(category);
    }

    @Override
    @Transactional
    public Product findProductById(Long id) {
        return productRepository.findById(id).get();
    }

    @Override
    public List<Product> getFeaturedProducts(int limit) {
        List<Product> allProducts = productRepository.findAll();
        return allProducts.subList(0, Math.min(limit, allProducts.size()));
    }
    
    /** Customer Service */
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer doesn't exist，Customer ID：" + customerId));
    }

    /** Review Service */
    public Review saveReview(Integer rating, String comment, Product product, Customer customer) {
    	// 1. Validate rating range
        if(rating == null || rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be 1 to 5");
        }
        // 2. Validate review content is not empty
        if(comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Review content cannot be empty");
        }
        // 3. Validate product and user are not null
        if(product == null) {
            throw new NoSuchElementException("Product does not exist");
        }
        if(customer == null) {
            throw new IllegalArgumentException("Please login first");
        }
        // 4. Construct review object and save
        Review review = new Review();
        review.setRating(rating);
        review.setComment(comment);
        review.setProduct(product);
        review.setCustomer(customer);
        return reviewRepository.save(review);
    }
    
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedTimeDesc(productId);
    }

}
