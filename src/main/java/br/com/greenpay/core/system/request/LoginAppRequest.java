package br.com.greenpay.core.system.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAppRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String login;
	private String senha;

}
