package br.com.greenpay.core.product.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadgeDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String text;
	private String color;

}
