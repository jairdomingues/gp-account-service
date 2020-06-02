package org.springframework.samples.petclinic.order.request;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.samples.petclinic.order.model.Payment.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private PaymentMethod paymentMethod;
	private BigDecimal amount;

}
