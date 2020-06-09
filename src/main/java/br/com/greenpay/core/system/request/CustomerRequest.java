package br.com.greenpay.core.system.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String document;
	private String firstname;
	private String lastname;
	private String uuidUser;
	private List<AddressRequest> adresses;
	private String password;
	private String userId;


}
