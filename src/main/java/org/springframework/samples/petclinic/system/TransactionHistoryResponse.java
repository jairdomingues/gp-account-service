package org.springframework.samples.petclinic.system;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.samples.petclinic.system.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.TransactionHistory.Status;
import org.springframework.samples.petclinic.system.TransactionHistory.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionHistoryResponse {

	private TransactionType transactionType;
	private Operation operation;
	private Status status;
	private BigDecimal amount;
	private Date transactionDate;
	private String history;
}
