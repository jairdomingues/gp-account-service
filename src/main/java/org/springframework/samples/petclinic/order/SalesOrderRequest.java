package org.springframework.samples.petclinic.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesOrderRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long clientRef;
	private Long partnerRef;
	private String cryptoRef;
	private Date saleDate;
	private List<PaymentRequest> payments;
	private Boolean ecommerce;

}
