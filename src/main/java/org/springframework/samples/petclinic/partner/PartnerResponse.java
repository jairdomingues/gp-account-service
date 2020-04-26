package org.springframework.samples.petclinic.partner;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String document;
	private String firstname;
	private String lastname;
	private String userId;
	private List<PartnerAddressResponse> adresses;

}
