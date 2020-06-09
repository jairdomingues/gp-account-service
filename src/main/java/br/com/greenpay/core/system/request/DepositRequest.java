package br.com.greenpay.core.system.request;

import java.io.Serializable;
import java.util.List;

import br.com.greenpay.core.order.request.PaymentRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String cpf;
	private List<PaymentRequest> payments;
	private String uuid;
}
