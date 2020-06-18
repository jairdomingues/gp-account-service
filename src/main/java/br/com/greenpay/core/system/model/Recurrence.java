package br.com.greenpay.core.system.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.greenpay.core.partner.model.Partner;
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
@Table(name = "RECURRENCE")
public class Recurrence extends BaseEntity {

	private String name;
	private Boolean active;
	private Boolean pending;

	private BigDecimal value;

	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date cancelDate;

	@ManyToOne
	private Partner partner;

}
