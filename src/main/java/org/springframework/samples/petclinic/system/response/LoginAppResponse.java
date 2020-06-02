package org.springframework.samples.petclinic.system.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginAppResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigo;
	private String mensagem;
	private String sucesso;
	private String resultado;

}
