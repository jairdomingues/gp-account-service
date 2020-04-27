package org.springframework.samples.petclinic.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.samples.petclinic.order.SalesOrder.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String customerName = "JAIR";
	private Status status;
	private Date saleDate;
	private Long clientRef;
	private Long partnerRef;
	private String cryptoRef;
	private List<PaymentRequest> payments;
	private BigDecimal totalAmount;
	

}
