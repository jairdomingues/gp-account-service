package org.springframework.samples.petclinic.system;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountValidRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long customerId;
	private String uuid;
	private String amount;
}
