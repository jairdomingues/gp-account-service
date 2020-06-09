package br.com.greenpay.core.webhook;

public class Holder {

	private String birthDate;
	private String birthdate;
	private String fullname;
	TaxDocument TaxDocumentObject;

	// Getter Methods

	public String getBirthDate() {
		return birthDate;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public String getFullname() {
		return fullname;
	}

	public TaxDocument getTaxDocument() {
		return TaxDocumentObject;
	}

	// Setter Methods

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public void setTaxDocument(TaxDocument taxDocumentObject) {
		this.TaxDocumentObject = taxDocumentObject;
	}
}
