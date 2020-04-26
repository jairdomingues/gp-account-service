package org.springframework.samples.petclinic.system;

import java.beans.Transient;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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
@Table(name = "CUSTOMER", uniqueConstraints = @UniqueConstraint(columnNames = "document"))
public class Customer extends BaseEntity {

	private String document;
	private String firstname;
	private String lastname;
	private String userId;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "walletOfCustomer")
    private WalletOfCustomer walletOfCustomer;
	
	@OneToMany(mappedBy = "customer")
	private List<Account> accounts;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "customer_id")
	private List<Address> adresses;

	@Transient
	public Address getAddress() {
		return this.adresses.parallelStream().filter(x -> x.getDefaults()).findFirst().get();
	}

}
