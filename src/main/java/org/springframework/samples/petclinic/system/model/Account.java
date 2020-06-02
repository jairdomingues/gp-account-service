package org.springframework.samples.petclinic.system.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "ACCOUNT")
@Inheritance(strategy = InheritanceType.JOINED)
public class Account extends BaseEntity {


	public static enum TypeAccount {
		WALLET("Wallet"), CURRENT_ACCOUNT("Conta Digital"), CREDIT_CARD("Cartão de Crédito");

		private final String value;

		private TypeAccount(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	private String name;
	private Boolean active;

	private BigDecimal lastBalance;

	@ManyToOne
	private Customer customer;

	@ManyToOne
	private WalletOfCustomer walletHolder;

	@OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id")
    @OrderBy("id DESC")	private List<TransactionHistory> transactions;

	@Transient
	public BigDecimal getBalance() {
		BigDecimal debitValue = new BigDecimal(0);
		BigDecimal creditValue = new BigDecimal(0);
		if (this.transactions != null && !this.transactions.isEmpty()) {
			List<TransactionHistory> listDebit = this.transactions.stream()
					.filter(x -> x.getTransactionType().equals(TransactionHistory.TransactionType.DEBIT)
							&& x.getStatus().equals(TransactionHistory.Status.ACTIVE))
					.collect(Collectors.toList());
			if (listDebit.size() > 0) {
				debitValue = listDebit.stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();
			}
			List<TransactionHistory> listCredit = this.transactions.stream()
					.filter(x -> x.getTransactionType().equals(TransactionHistory.TransactionType.CREDIT)
							&& x.getStatus().equals(TransactionHistory.Status.ACTIVE))
					.collect(Collectors.toList());
			if (listCredit.size() > 0) {
				creditValue = listCredit.stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();
			}
		}
		return creditValue.subtract(debitValue);
	}

	@Transient
	public TypeAccount getTypeAccount() {
		TypeAccount typeAccount = null;
		if (this instanceof CurrentAccount) {
			typeAccount = TypeAccount.CURRENT_ACCOUNT;
		} else if (this instanceof Wallet) {
			typeAccount = TypeAccount.WALLET;
		} else if (this instanceof CreditCard) {
			typeAccount = TypeAccount.CREDIT_CARD;
		}
		return typeAccount;
	}
}

