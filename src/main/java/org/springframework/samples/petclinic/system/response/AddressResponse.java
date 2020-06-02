package org.springframework.samples.petclinic.system.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String street;
	private String number;
	private String complement;
	private String neighborhood;
	private String city;
	private String province;
	private String zip;
	private String country;
	private String lat;
	private String lon;
	private String referencePoint;
	private Boolean defaults;
	
}
