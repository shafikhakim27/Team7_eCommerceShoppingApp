// package sg.edu.nus.caproject.model;
package com.ecommerce.model;

import java.time.YearMonth;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Payment {
	@NotBlank (message="Name is required.")
	private String name;
	
	@NotBlank(message="Credit Card Number is required.")
	@Size(min=16, max=16, message="Credit Card Number must contain 16 digits.")
	private String ccNumber;
	
	@NotNull (message="Expiry Date is required.")
	@DateTimeFormat (pattern = "MM-yyyy")
	private YearMonth expiryDate;
	
	public YearMonth getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(YearMonth expiryDate) {
		this.expiryDate = expiryDate;
	}

	@NotBlank (message="CVV is required.")
	@Size(min=3, max=3, message="Please enter valid CVV.")
	private String CVV;
	
	private double totalAmount;

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCcNumber() {
		return ccNumber;
	}

	public void setCcNumber(String ccNumber) {
		this.ccNumber = ccNumber;
	}


	public String getCVV() {
		return CVV;
	}

	public void setCVV(String cVV) {
		CVV = cVV;
	}
}
