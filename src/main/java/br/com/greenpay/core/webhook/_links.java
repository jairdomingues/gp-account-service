package br.com.greenpay.core.webhook;

public class _links {
	
	Order OrderObject;
	Self SelfObject;

	// Getter Methods

	public Order getOrder() {
		return OrderObject;
	}

	public Self getSelf() {
		return SelfObject;
	}

	// Setter Methods

	public void setOrder(Order orderObject) {
		this.OrderObject = orderObject;
	}

	public void setSelf(Self selfObject) {
		this.SelfObject = selfObject;
	}
}
