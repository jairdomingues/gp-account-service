package org.springframework.samples.petclinic.webhook;

public class Resource {

	Payment PaymentObject;

	// Getter Methods

	public Payment getPayment() {
		return PaymentObject;
	}

	// Setter Methods

	public void setPayment(Payment paymentObject) {
		this.PaymentObject = paymentObject;
	}
}
