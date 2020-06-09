package br.com.greenpay.core.order.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaymentResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;
	private String transactionCrypto;
	private String transactionAccount;
	private String transactionCredit;

}
