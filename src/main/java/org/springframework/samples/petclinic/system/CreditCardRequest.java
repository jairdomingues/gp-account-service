package org.springframework.samples.petclinic.system;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreditCardRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String cardNumber;
	private String brand;
	private String cardholderName;
	private String expirationMonth;
	private String expirationYear;
	private String securityCode;
	private CustomerResponse customer;

}
