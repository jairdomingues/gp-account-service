package br.com.greenpay.core.partner.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerAddressResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String number;
	private String street;
	private String complement;
	private String neighborhood;
	private String city;
	private String province;
	private String zip;
	private String country;
	private String lat;
	private String lng;
	private String referencePoint;
	private Boolean defaults;

	
}
