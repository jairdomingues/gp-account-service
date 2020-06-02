package org.springframework.samples.petclinic.system.request;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.samples.petclinic.system.model.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.model.TransactionHistory.Status;
import org.springframework.samples.petclinic.system.model.TransactionHistory.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionHistoryRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long idAccount; 
	private Operation operation; 
	private TransactionType transactionType;
	private Status status;
	private String history;
	private BigDecimal amount; 
	private Long orderId;
	
}
