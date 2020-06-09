package br.com.greenpay.core.system.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "WALLET_OF_CUSTOMER")
public class WalletOfCustomer extends BaseEntity {

	private String password;
	
    @OneToMany(mappedBy = "walletHolder")
    private List<Account> accountsInWallet;

    @OneToOne
    private Customer walletOfCustomer;
    
}
