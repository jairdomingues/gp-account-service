package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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
@Table(name = "PAYMENT")
public class Payment extends BaseEntity {

	
	public static enum PaymentMethod {
		WALLET("Wallet"), CURRENT_ACCOUNT("Conta Digital"), CREDIT_CARD("Cartão de Crédito");

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
