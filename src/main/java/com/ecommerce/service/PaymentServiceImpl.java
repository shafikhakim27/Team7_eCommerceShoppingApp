//package name here
package com.ecommerce.service;

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.ecommerce.model.Payment;
import java.time.YearMonth;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService{
//main responsibility is to handle payment processing logic
// eg calling payment gateway, through here it will mainly be validation

	public boolean processPayment (Payment payment, double amount) {
		//minimal check as payment simulation
		if (amount<=0) {
			return false;
		}
		
		// Basic payment validation
		if (payment == null) {
			return false;
		}
		
		// Check if card is expired
		if (payment.getExpiryDate() != null && payment.getExpiryDate().compareTo(YearMonth.now()) < 0) {
			return false; // Card is expired
		}
		
		// Check if required fields are present
		if (payment.getName() == null || payment.getName().trim().isEmpty()) {
			return false;
		}
		
		if (payment.getCcNumber() == null || payment.getCcNumber().length() != 16) {
			return false;
		}
		
		return true;
	}
}
