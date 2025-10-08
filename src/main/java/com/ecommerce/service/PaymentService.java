//package name here

import sg.edu.nus.caproject.model.Payment;

public interface PaymentService {
	//main responsibility is to handle payment processing logic
	boolean processPayment (Payment payment, double amount);
}
