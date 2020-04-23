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
	//current account
	private String bankCode;
	private String agency;
	private String currentAccount;
	//wallet
	private String hashCard;
	//cartao de credito
	private String cardNumber;
	private String brand;
	private String cardholderName;
	private String expirationMonth;
	private String expirationYear;
	private String securityCode;
	

}
