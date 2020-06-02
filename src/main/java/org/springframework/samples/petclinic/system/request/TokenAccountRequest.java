package org.springframework.samples.petclinic.system.request;

import java.io.Serializable;

import org.springframework.samples.petclinic.system.model.TokenAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private TokenAccount.Type type;
	private Long accountId;
	private String password;
}
