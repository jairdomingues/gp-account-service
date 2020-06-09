package br.com.greenpay.core.webhook;

public class CreditCard {

	private String brand;
	private String first6;
	Holder HolderObject;
	private String last4;
	private boolean store;

	// Getter Methods

	public String getBrand() {
		return brand;
	}

	public String getFirst6() {
		return first6;
	}

	public Holder getHolder() {
		return HolderObject;
	}

	public String getLast4() {
		return last4;
	}

	public boolean getStore() {
		return store;
	}

	// Setter Methods

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public void setFirst6(String first6) {
		this.first6 = first6;
	}

	public void setHolder(Holder holderObject) {
		this.HolderObject = holderObject;
	}

	public void setLast4(String last4) {
		this.last4 = last4;
	}

	public void setStore(boolean store) {
		this.store = store;
	}
}
