package org.springframework.samples.petclinic.partner.request;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String phone;
	private String password;
	private Date birthday;
	private Set<String> role;

}
