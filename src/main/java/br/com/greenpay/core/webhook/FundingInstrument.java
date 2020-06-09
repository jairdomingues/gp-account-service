package br.com.greenpay.core.webhook;

public class FundingInstrument {

	CreditCard CreditCardObject;
	private String method;

	// Getter Methods

	public CreditCard getCreditCard() {
		return CreditCardObject;
	}

	public String getMethod() {
		return method;
	}

	// Setter Methods

	public void setCreditCard(CreditCard creditCardObject) {
		this.CreditCardObject = creditCardObject;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}
