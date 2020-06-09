package br.com.greenpay.core.system.response;

import java.math.BigDecimal;
import java.util.Date;

import br.com.greenpay.core.system.model.TransactionHistory.Operation;
import br.com.greenpay.core.system.model.TransactionHistory.Status;
import br.com.greenpay.core.system.model.TransactionHistory.TransactionType;
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
