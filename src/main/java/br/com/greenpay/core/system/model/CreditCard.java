package br.com.greenpay.core.system.model;

import javax.persistence.Entity;
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
@Table(name = "CREDIT_CARD", uniqueConstraints = @UniqueConstraint(columnNames = "cardNumber"))
public class CreditCard extends Account {

	private String cardNumber;
	private String brand;
	private String cardholderName;
	private String expirationMonth;
	private String expirationYear;
	private String securityCode;
	
}
