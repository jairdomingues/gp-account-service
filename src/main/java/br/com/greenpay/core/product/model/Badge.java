package br.com.greenpay.core.product.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "BADGE")
public class Badge extends BaseEntity {

	@Column(name = "text")
	private String  text;

	@Column(name = "color")
	private String color;

}
