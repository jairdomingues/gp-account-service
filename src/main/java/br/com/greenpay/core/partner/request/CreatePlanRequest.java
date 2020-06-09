package br.com.greenpay.core.partner.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePlanRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	private ContactPlanRequest contact;
	private CardRequest card;

}
