package org.springframework.samples.petclinic.product;

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
@Table(name = "PRICE")
public class Price extends BaseEntity {

	@Column(name = "sale", precision = 10, scale = 2, nullable = false)
	private BigDecimal sale;

	@Column(name = "previous", precision = 10, scale = 2, nullable = false)
	private BigDecimal previous;

}
