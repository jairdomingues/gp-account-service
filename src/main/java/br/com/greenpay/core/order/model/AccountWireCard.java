package br.com.greenpay.core.order.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.greenpay.core.partner.model.Partner;
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
