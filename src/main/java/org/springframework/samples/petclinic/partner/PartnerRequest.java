package org.springframework.samples.petclinic.partner;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String document;
	private String firstname;
	private String lastname;
	private String userId;
	private PartnerAccountRequest account;
	private List<PartnerAddressRequest> adresses;

}
