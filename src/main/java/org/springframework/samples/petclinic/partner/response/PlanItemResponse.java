package org.springframework.samples.petclinic.partner.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanItemResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String description;
	private Boolean active;

}
