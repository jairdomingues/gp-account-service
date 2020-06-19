package br.com.greenpay.core.partner.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.greenpay.core.partner.PartnerAccount;
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
@Table(name = "PARTNER")
public class Partner extends BaseEntity {

	private String document;
	private String fantasia;
	private String razaoSocial;
	private String userId;
	private String email;
	private String deltaId;
	private Date deltaDate;
	private Boolean activatedPlan;
	
	@OneToOne
	private PartnerAccount account;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "partner_id")
	private List<PartnerAddress> adresses;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "partner_id")
	private List<PartnerContact> contacts;

	@ManyToOne
	private ActivityBranch activityBranch;

	@ManyToOne
	private Plan plan;

}
