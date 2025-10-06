package com.ecommerce.model;

import jakarta.persistence.*;

@Entity
public class OrderItem {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private int quantity;
	private double unitPrice; //at the time of order

	//constructor
	public OrderItem(Order order, Product product, int quantity, double unitPrice) {
		this.order= order;
		this.product= product;
		this.quantity= quantity;
		this.unitPrice= unitPrice;
	}
	
	OrderItem(){}
	
	@ManyToOne
	@JoinColumn (name="order_id")
	private Order order;

	@ManyToOne
	@JoinColumn(name="product_id")
	private Product product;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
