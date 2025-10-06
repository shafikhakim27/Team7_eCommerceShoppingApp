package com.ecommerce.model;

import java.util.List;
import jakarta.persistence.*;

/**
 * Product entity representing an e-commerce product.
 * Updated for Java 21 compatibility with modern features.
 */
@Entity
@Table(name = "product")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length=50)
    private String name;
    
    @Column(length = 1000)
    private String description;

    @Column (length = 20)
    private String imageName;
    
    @Column(nullable = false, length=20)
    private String category;
    
    @Column(nullable = false)
    private Double price;

    @Column (length = 20)
    private String brand;

    // Constructor
    public Product() {
		super();
	}
	
	public Product(String name, String description, String imageName, 
			String category, double price, String brand) {
		super();
		this.name = name;
		this.description = description;
		this.imageName = imageName;
		this.category = category;
		this.price = price;
		this.brand = brand;
	}

    // Getters and setters
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;

    public List<CartItem> getCartItems(){
		return cartItems;
	}
	
	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@OneToMany (mappedBy="product")
	private List<OrderItem> orderItems;
	
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
    
    //@ManyToMany(mappedBy = "orderProducts")
    //private List<Order> orders;

    // public List<Order> getOrders() {
    //    return orders;
    //}
    
    //public void setOrders(List<Order> orders) {
    //    this.orders = orders;
    //}

    // KIV TO REMOVE FROM HERE
    @ManyToMany(mappedBy = "customerProducts")
    private List<Customer> customers;
    
    public List<Customer> getCustomers() {
        return customers;
    }
    
    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
    // TO HERE
    
    @Override
    public String toString() {
        return "Product{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", productPrice=" + productPrice +
                ", productBrand='" + productBrand + '\'' +
                '}';
    }
}
