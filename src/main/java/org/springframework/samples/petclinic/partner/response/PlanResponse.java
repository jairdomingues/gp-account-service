package org.springframework.samples.petclinic.partner.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.samples.petclinic.partner.model.Plan.Operation;

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
	private List<PlanItemResponse> items;

}
