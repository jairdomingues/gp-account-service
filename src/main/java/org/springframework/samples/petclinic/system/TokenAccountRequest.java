package org.springframework.samples.petclinic.system;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountId;
	private String password;
}
