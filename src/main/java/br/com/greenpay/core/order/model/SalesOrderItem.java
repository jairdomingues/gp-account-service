package br.com.greenpay.core.order.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.greenpay.core.product.model.Product;
import br.com.greenpay.core.system.model.BaseEntity;
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
@Table(name = "SALES_ORDER_ITEM")
public class SalesOrderItem extends BaseEntity {

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

	public BigDecimal value;
	
	@ManyToOne
	private Product product;
	
	
}
