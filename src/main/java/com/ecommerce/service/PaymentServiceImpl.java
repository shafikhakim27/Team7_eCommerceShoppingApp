//package name here

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import sg.edu.nus.caproject.model.Payment;

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
		
		return true;
	}	
}
