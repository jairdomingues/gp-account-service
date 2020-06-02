package org.springframework.samples.petclinic.partner.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.samples.petclinic.partner.model.ReleaseHistory.Operation;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory.Status;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory.TransactionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReleaseHistoryResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private TransactionType transactionType;
	private Operation operation;
	private Status status;
	private BigDecimal amount;
	private Date transactionDate;
	private String history;

}
