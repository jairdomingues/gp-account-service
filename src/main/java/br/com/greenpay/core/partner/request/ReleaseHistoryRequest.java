package br.com.greenpay.core.partner.request;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.greenpay.core.partner.model.ReleaseHistory.Operation;
import br.com.greenpay.core.partner.model.ReleaseHistory.Status;
import br.com.greenpay.core.partner.model.ReleaseHistory.TransactionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseHistoryRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private TransactionType transactionType;
	private Operation operation;
	private Status status;
	private BigDecimal amount;
	private Date transactionDate;
	private String history;

}
