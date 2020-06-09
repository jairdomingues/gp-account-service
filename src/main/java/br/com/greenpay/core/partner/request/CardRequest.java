package br.com.greenpay.core.partner.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String brand;
	private String cardholderName;
	private String cardNumber;
	private String expirationMonth;
	private String expirationYear;
	private String securityCode;

}
