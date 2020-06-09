package br.com.greenpay.core.partner.model;

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
@Table(name = "PARTNER_CONTACT")
public class PartnerContact extends BaseEntity {

	private String name;
	private String email;
	private String document;

}
