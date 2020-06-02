package org.springframework.samples.petclinic.partner.request;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.samples.petclinic.partner.model.Plan.Operation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Operation operation;
	private String name;
	private String description;
	private Boolean active;
	private BigDecimal value;

}
