package org.springframework.samples.petclinic.system;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "TRANSACTION_HISTORY")
public class TransactionHistory extends BaseEntity {

	public static enum Operation {
		PAYMENT("payment"), WITHDRAW("withdraw"), DEPOSIT("deposit"), TRANSFER("transfer"),
		SHARE("Indicação");

		private final String value;

		private Operation(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
    
	public static enum TransactionType {
		DEBIT("Debit"), CREDIT("Credit");

		private final String value;

		private TransactionType(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	public static enum Status {
		ACTIVE("Active"), PREVIOUS("Previous"), Estorno("Estorno"),
		BLOCK("Bloqueado");

		private final String value;

		private Status(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "transactionType", nullable = false)
	private TransactionType transactionType;

	@Enumerated(EnumType.STRING)
	@Column(name = "operation", nullable = false)
	private Operation operation;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;
	
	@Column(name = "amount", precision = 10, scale = 2)
	private BigDecimal amount;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;

	private String history;

}
