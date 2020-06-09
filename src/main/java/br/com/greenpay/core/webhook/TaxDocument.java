package br.com.greenpay.core.webhook;

public class TaxDocument {

	private String number;
	private String type;

	// Getter Methods

	public String getNumber() {
		return number;
	}

	public String getType() {
		return type;
	}

	// Setter Methods

	public void setNumber(String number) {
		this.number = number;
	}

	public void setType(String type) {
		this.type = type;
	}
}
