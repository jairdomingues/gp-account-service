package org.springframework.samples.petclinic.partner.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.system.model.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "PLAN_ITEM")
public class PlanItem extends BaseEntity {

	private String name;
	private String description;
	private Boolean active;

}
