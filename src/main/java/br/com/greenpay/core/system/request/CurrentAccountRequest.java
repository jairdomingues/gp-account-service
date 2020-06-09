package br.com.greenpay.core.system.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAccountRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String bankCode;
	private String agency;
	private String currentAccount;
	private CustomerRequest customer;

}
