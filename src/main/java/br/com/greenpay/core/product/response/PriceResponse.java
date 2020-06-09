package br.com.greenpay.core.product.response;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceResponse {

	private BigDecimal sale;
	private BigDecimal previous;

}
