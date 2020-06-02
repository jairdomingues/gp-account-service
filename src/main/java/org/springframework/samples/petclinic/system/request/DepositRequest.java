package org.springframework.samples.petclinic.system.request;

import java.io.Serializable;
import java.util.List;

import org.springframework.samples.petclinic.order.request.PaymentRequest;

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
