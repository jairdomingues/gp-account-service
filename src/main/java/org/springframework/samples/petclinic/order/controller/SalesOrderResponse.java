package org.springframework.samples.petclinic.order.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.samples.petclinic.order.model.SalesOrder.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String customerName = "JAIR";
	private Status status;
	private Long clientRef;
	private Long partnerRef;
	private List<PaymentRequest> payments;
	private BigDecimal totalAmount;
	

}
