package br.com.greenpay.core.system.request;

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
	private CustomerRequest customer;

}
