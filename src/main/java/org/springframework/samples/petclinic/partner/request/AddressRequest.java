package org.springframework.samples.petclinic.partner.request;

import java.util.List;

import org.springframework.samples.petclinic.partner.model.Address;

public class AddressRequest {

	private List<Address> adresses;

	public List<Address> getAdresses() {
		return adresses;
	}

	public void setAdresses(List<Address> adresses) {
		this.adresses = adresses;
	}

}
