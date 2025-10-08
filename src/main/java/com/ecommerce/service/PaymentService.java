//package name here
package com.ecommerce.service;

// import sg.edu.nus.caproject.model.Payment;
// import com.ecommerce.model.Payment;

public interface PaymentService {
	//main responsibility is to handle payment processing logic
	boolean processPayment (Payment payment, double amount);
}
