package org.springframework.samples.petclinic.order;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long clientRef;
	private Long partnerRef;
	private List<PaymentRequest> payments;

}
