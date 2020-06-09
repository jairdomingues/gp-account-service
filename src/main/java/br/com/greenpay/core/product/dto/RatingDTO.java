package br.com.greenpay.core.product.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import br.com.greenpay.core.system.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class RatingDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal rating;
	private BigDecimal ratingCount;

}
