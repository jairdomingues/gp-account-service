package br.com.greenpay.core.partner.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import br.com.greenpay.core.partner.model.Plan.Operation;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Operation operation;
	private String name;	
	private String description;
	private Boolean active;
	private BigDecimal value;
	private BigDecimal memberFee;
	private List<PlanItemResponse> items;

}
