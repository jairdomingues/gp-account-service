package br.com.greenpay.core.partner.request;

import java.util.List;

import br.com.greenpay.core.partner.model.Address;

public class AddressRequest {

	private List<Address> adresses;

	public List<Address> getAdresses() {
		return adresses;
	}

	public void setAdresses(List<Address> adresses) {
		this.adresses = adresses;
	}

}
