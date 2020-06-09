package br.com.greenpay.core.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal sale;
	private BigDecimal previous;

}
