package br.com.greenpay.core.system.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenAccountValidRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long customerId;
	private String uuid;
	private Long orderId;
	
}
