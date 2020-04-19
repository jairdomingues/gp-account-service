package org.springframework.samples.petclinic.order.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.samples.petclinic.system.TransactionHistory;

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
@Table(name = "PAYMENT")
public class Payment extends BaseEntity {

	public static enum PaymentMethod {
		CryptoCurrency("Crypto Moeda"), CreditCard("Cartão de Crédito"), CurrentAccount("Conta Digital");

		private final String value;

		private PaymentMethod(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method", nullable = false)
	private PaymentMethod paymentMethod;
	
	@Column(name = "amount", precision = 10, scale = 2, nullable = false)
	private BigDecimal amount;

}
