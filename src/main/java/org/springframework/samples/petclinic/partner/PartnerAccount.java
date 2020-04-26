package org.springframework.samples.petclinic.partner;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

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
@Table(name = "PARTNER_ACCOUNT")
public class PartnerAccount extends BaseEntity {

	private String name;
	private Boolean active;
	private BigDecimal lastBalance;

	@ManyToOne
	private Partner partner;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "partner_id")
	@OrderBy("id DESC")
	private List<ReleaseHistory> releases;

	@Transient
	public BigDecimal getBalance() {
		BigDecimal debitValue = new BigDecimal(0);
		BigDecimal creditValue = new BigDecimal(0);
		if (this.releases != null && !this.releases.isEmpty()) {
			List<ReleaseHistory> listDebit = this.releases.stream()
					.filter(x -> x.getTransactionType().equals(ReleaseHistory.TransactionType.DEBIT)
							&& x.getStatus().equals(ReleaseHistory.Status.ACTIVE))
					.collect(Collectors.toList());
			if (listDebit.size() > 0) {
				debitValue = listDebit.stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();
			}
			List<ReleaseHistory> listCredit = this.releases.stream()
					.filter(x -> x.getTransactionType().equals(ReleaseHistory.TransactionType.CREDIT)
							&& x.getStatus().equals(ReleaseHistory.Status.ACTIVE))
					.collect(Collectors.toList());
			if (listCredit.size() > 0) {
				creditValue = listCredit.stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();
			}
		}
		return creditValue.subtract(debitValue);
	}

}
