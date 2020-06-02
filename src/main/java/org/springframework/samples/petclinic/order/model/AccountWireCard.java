package org.springframework.samples.petclinic.order.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.partner.model.Partner;
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
@Table(name = "ACCOUNT_WIRE_CARD")
public class AccountWireCard extends BaseEntity {

	private String _id;
	private String accessToken;
	private Boolean transparentAccount;
	private String email;
	private String login;

	@ManyToOne
	private Partner partner;

}
