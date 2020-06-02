package org.springframework.samples.petclinic.system.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
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
@Table(name = "TOKEN_ACCOUNT")
public class TokenAccount extends BaseEntity {

	public static enum Type {
		WALLET("Wallet"), TRANSFER("Transferẽncia"), SHARE("Indicação");

		private final String value;

		private Type(final String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type", nullable = false)
	private Type type;
	
	@Column(name = "valid", nullable = false)
	private Boolean valid;

	@ManyToOne
	private Account account;

	@Transient
	public Boolean getExpired() {

		Boolean expired = false;
		LocalDate now = LocalDate.now();
		LocalDate created = Instant.ofEpochMilli(this.createDate.getTime()).atZone(ZoneId.systemDefault())
				.toLocalDate();

		Duration duration = Duration.between(created, now);
		if (duration.toDays() > 1) {
			expired = true;
		}

		return expired;
	}

}
