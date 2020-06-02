package org.springframework.samples.petclinic.system.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String hashCard;
	private CustomerRequest customer;

}
