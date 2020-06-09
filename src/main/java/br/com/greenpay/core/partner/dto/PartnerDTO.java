package br.com.greenpay.core.partner.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String document;
	private String fantasia;
	private String razaoSocial;
	private String userId;
	private String deltaId;
	private Date deltaDate;

}
