package br.com.greenpay.core.product.dto;

import java.io.Serializable;

import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "text")
	private String text;

}
