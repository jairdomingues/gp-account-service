package org.springframework.samples.petclinic.system.model;

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
@Table(name = "CURRENT_ACCOUNT", 
uniqueConstraints = { 
		@UniqueConstraint(columnNames = {"bankCode", "agency", "currentAccount"})})
public class CurrentAccount extends Account {

	private String bankCode;
	private String agency;
	private String currentAccount;
	
}
