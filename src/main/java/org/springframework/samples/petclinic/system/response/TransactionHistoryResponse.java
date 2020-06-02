package org.springframework.samples.petclinic.system.response;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.samples.petclinic.system.model.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.model.TransactionHistory.Status;
import org.springframework.samples.petclinic.system.model.TransactionHistory.TransactionType;

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
