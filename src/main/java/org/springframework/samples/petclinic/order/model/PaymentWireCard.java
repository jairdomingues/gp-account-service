package org.springframework.samples.petclinic.order.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.samples.petclinic.partner.model.Partner;
import org.springframework.samples.petclinic.system.model.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "PAYMENT_WIRE_CARD")
public class PaymentWireCard extends BaseEntity {

	private String _id;
	private String status;
	private String amountPayment;
	private String amountReceiver1;
	private String amountReceiver2;

	@ManyToOne
	private Partner partner;

	
}
