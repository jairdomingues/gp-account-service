package br.com.greenpay.core.order.request;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.com.greenpay.core.product.request.ProductRequest;
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
	private List<ProductRequest> products;
	private Boolean ecommerce;

}
