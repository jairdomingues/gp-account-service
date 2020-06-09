package br.com.greenpay.core.webhook;

public class AcquirerDetails {
	
	private String authorizationNumber;
	TaxDocument TaxDocumentObject;

	// Getter Methods

	public String getAuthorizationNumber() {
		return authorizationNumber;
	}

	public TaxDocument getTaxDocument() {
		return TaxDocumentObject;
	}

	// Setter Methods

	public void setAuthorizationNumber(String authorizationNumber) {
		this.authorizationNumber = authorizationNumber;
	}

	public void setTaxDocument(TaxDocument taxDocumentObject) {
		this.TaxDocumentObject = taxDocumentObject;
	}
}
