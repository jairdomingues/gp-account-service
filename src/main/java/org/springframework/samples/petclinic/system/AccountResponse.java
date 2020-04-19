package org.springframework.samples.petclinic.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private BigDecimal lastBalance;
	private List<TransactionHistoryResponse> transactions;
	private BigDecimal balance;
	private Date createDate; 
	private String bankCode;
	private String agency;
	private String currentAccount;
	private String hashCard;

}
