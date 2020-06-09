package br.com.greenpay.core.partner.request;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String document;
	private String fantasia;
	private String razaoSocial;
	private String userId;
	private PartnerAccountRequest account;
	private List<PartnerAddressRequest> adresses;

}
