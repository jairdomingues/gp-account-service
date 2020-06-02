package org.springframework.samples.petclinic.partner.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerAccountRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private Boolean active;
	private BigDecimal lastBalance;
	private PartnerRequest partner;
	private List<ReleaseHistoryRequest> releases;
	private BigDecimal balance;

}
