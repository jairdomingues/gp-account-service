package org.springframework.samples.petclinic.system.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CurrentAccountResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String bankCode;
	private String agency;
	private String currentAccount;

}
