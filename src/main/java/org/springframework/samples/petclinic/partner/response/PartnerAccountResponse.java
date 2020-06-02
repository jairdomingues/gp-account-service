package org.springframework.samples.petclinic.partner.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerAccountResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Boolean active;
	private BigDecimal lastBalance;
	private PartnerResponse partner;
	private List<ReleaseHistoryResponse> releases;
	private BigDecimal balance;

}
