package org.springframework.samples.petclinic.partner.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPlanRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String firstname;
	private String lastname;
	private String cpf;
	private String email;
	
	
}
