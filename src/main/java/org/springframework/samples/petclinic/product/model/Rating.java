package org.springframework.samples.petclinic.product.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "RATING")
public class Rating extends BaseEntity {

	@Column(name = "rating", precision = 10, scale = 2, nullable = false)
	private BigDecimal rating;

	@Column(name = "rating_count", precision = 10, scale = 2, nullable = false)
	private BigDecimal ratingCount;

}
