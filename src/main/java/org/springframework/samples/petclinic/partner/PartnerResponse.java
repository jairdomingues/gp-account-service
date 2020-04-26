package org.springframework.samples.petclinic.partner;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.samples.petclinic.order.SalesOrder.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartnerResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

}
