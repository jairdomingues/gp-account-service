package br.com.greenpay.core.system.response;

import br.com.moip.resource.Payment;

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
