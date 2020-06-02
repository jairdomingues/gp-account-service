package org.springframework.samples.petclinic.partner.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "PLAN")
public class Plan extends BaseEntity {

	public static enum Operation {
		MONTH("Mensal"), QUARTER("Quadrimestral"), YEAR("Anual");

		private final String value;

		private Operation(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "operation", nullable = false)
	private Operation operation;

	private String name	;
	private String description;
	private Boolean active;
	
	@Column(name = "amount", precision = 10, scale = 2)
	private BigDecimal value;

	@Column(name = "member_fee", precision = 10, scale = 2)
	private BigDecimal memberFee;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "plan_id")
	private List<PlanItem> items;
	
}
