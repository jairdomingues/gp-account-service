package org.springframework.samples.petclinic.system.response;

import java.io.Serializable;

import org.springframework.samples.petclinic.system.model.TokenAccount;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private TokenAccount.Type type;
	private String uuid;

}
