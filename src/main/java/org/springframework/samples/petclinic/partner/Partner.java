package org.springframework.samples.petclinic.partner;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "PARTNER")
public class Partner extends BaseEntity {

	private String document;
	private String firstname;
	private String lastname;
	private String userId;

	@ManyToOne
	private PartnerAccount account;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "partner_id")
	private List<PartnerAddress> adresses;

}
