package br.com.greenpay.core.partner.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String document;
	private String fantasia;
	private String razaoSocial;
	private String userId;
	private List<PartnerAddressResponse> adresses;
	private PlanResponse plan;
	private ActivityBranchResponse activityBranch;

}
