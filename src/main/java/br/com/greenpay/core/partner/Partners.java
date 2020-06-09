package br.com.greenpay.core.partner;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Partners implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String fantasia;
	private String razaosocial;
	private String cnpj;
	private String cpf;
	private String contato;
	private String email;
	private String endereco;
	private String endereconumero;
	private String enderecocomplemento;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private String telefoneprincipal;
	private String telefonebackup;
	private String clientestatus;
	private String ramoatividade;
	private String datacadastro;

}
