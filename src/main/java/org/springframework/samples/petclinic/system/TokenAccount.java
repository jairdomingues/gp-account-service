package org.springframework.samples.petclinic.system;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.persistence.Column;
import javax.persistence.Entity;
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
