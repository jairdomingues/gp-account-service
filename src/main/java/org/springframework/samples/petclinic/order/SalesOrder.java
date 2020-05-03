package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.samples.petclinic.system.BaseEntity;

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
@Table(name = "SALES_ORDER")
public class SalesOrder extends BaseEntity {

	public static enum Status {
		PENDING("Pendente"), DRAFT("Rascunho"), PAID("Pago"), CANCELED("Cancelado");

		private final String value;

		private Status(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "client_ref", nullable = true)
	private Long clientRef;

	@Column(name = "partner_ref", nullable = false)
	private Long partnerRef;

	@Column(name = "crypto_ref")
	private String cryptoRef;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date saleDate;

	@OneToMany(cascade = CascadeType.ALL)
	private List<Payment> payments;
	
	@Transient
	public BigDecimal getTotalAmount() {
		return this.payments.stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();
	}
	

}
