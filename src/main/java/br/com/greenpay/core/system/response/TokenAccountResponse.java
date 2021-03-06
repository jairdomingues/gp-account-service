package br.com.greenpay.core.system.response;

import java.io.Serializable;

import br.com.greenpay.core.system.model.TokenAccount;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private TokenAccount.Type type;
	private String uuid;

}
