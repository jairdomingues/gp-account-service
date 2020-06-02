package org.springframework.samples.petclinic.wirecard;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountWireCardRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	public static enum Type {
		ACCOUNT("ACCOUNT"), RECURRENCE("RECURRENCE"), MEMBER_FEE("MEMBER_FEE"), CREDIT("CREDIT");

		private final String value;

		private Type(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	private Type type;
	
	//account
	private String email;
	private String name;
	private String lastName;
	private String cpf;
	private String street;
	private String streetNumber;
	private String district;
	private String city;
	private String state;
	private String country;
	private String zipCode;
	private String countryCode;
	private String areaCode;
	private String number;
	//plan
	private String product;
	private String value;
	
	private String moipAccount;

}
