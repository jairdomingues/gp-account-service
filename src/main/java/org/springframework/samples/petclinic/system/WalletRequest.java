package org.springframework.samples.petclinic.system;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String hashCard;
	private CustomerResponse customer;

}
