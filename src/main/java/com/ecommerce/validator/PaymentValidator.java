//package name here
package com.ecommerce.validator;

import java.time.YearMonth;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

// import sg.edu.nus.caproject.model.Payment;
// import com.ecommerce.model.Payment;

@Component
public class PaymentValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Payment.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Payment payment = (Payment) obj;
		YearMonth currentYearMonth = YearMonth.now();
		if ((payment.getExpiryDate() != null) && (payment.getExpiryDate().compareTo(currentYearMonth)<0)){
			errors.rejectValue("expiryDate", "error.expired", "Card is expired.");
			//this means field expiryDate has an issue, tag it with error code "error.expired" and show message "Card is expired."
		}
	} 
}

