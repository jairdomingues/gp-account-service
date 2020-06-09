package br.com.greenpay.core.partner.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityBranchResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;

}
