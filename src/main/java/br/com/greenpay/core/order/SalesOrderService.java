package br.com.greenpay.core.order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.greenpay.core.order.model.SalesOrder;
import br.com.greenpay.core.order.repository.SalesOrderRepository;
import br.com.greenpay.core.order.request.SalesOrderRequest;
import br.com.greenpay.core.order.response.ResultPaymentResponse;
import br.com.greenpay.core.order.response.SalesOrderResponse;
import br.com.greenpay.core.partner.model.Partner;
import br.com.greenpay.core.partner.repository.PartnerRepository;
import br.com.greenpay.core.product.model.Product;
import br.com.greenpay.core.product.repository.ProductRepository;
import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;

@Service
@Transactional
public class SalesOrderService {

	private static final Log LOGGER = LogFactory.getLog(SalesOrderService.class);

	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private PartnerRepository partnerRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PaymentService paymentService;
	
	private Product convert(Product product) {
		return productRepository.findById(product.getId()).orElse(null);
	}
	
	public SalesOrderResponse createOrder(SalesOrderRequest salesOrderRequest) {
		
		Partner partner = partnerRepository.findById(salesOrderRequest.getPartnerRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));

		SalesOrder salesOrder = this.convertToSalesOrder(salesOrderRequest);
		salesOrder.setStatus(SalesOrder.Status.PENDING);
		salesOrder.getProducts().stream().forEach(i -> i.setPartner(partner));
		List<Product> listProducts = new ArrayList<Product>();
		for (Product product : salesOrder.getProducts()) {
			product = productRepository.findById(product.getId()).orElse(null);
			listProducts.add(product);
		}
		salesOrder.setProducts(new ArrayList<Product>());
		salesOrder.setProducts(listProducts);
		salesOrderRepository.save(salesOrder);
		if (salesOrderRequest.getEcommerce()) {
			ResultPaymentResponse response = paymentService.paymentSalesOrder(salesOrder.getId(), salesOrderRequest);
			if (response.getStatus()!=null && response.getStatus().equalsIgnoreCase("OK")) {
				salesOrder.setStatus(SalesOrder.Status.PAID);
				salesOrder.setSaleDate(new Date());
				salesOrder.setCryptoRef(response.getTransactionCrypto());
				salesOrderRepository.save(salesOrder);
			} else {
				throw new CustomGenericNotFoundException("Error: Payments is invalid.");
			}
		}
		return convertToSalesOrderResponse(salesOrder);
	}

	public List<SalesOrderResponse> findAllOrders() {
		List<SalesOrder> salesOrders = (List<SalesOrder>) salesOrderRepository.findAll();
		return salesOrders.stream().sorted(Comparator.comparing(SalesOrder::getCreateDate).reversed())
				.map(this::convertToSalesOrderResponse).collect(Collectors.toList());
	}

	public SalesOrderResponse findOrderById(Long orderId) {
		SalesOrder salesOrder = salesOrderRepository.findById(orderId)
		.orElseThrow(() -> new CustomGenericNotFoundException("Error: SalesOrder is not found."));
		return convertToSalesOrderResponse(salesOrder);
	}

	public void deleteById(Long orderId) {
		SalesOrder salesOrder = salesOrderRepository.findById(orderId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: SalesOrder is not found."));
		salesOrderRepository.delete(salesOrder);
	}
	

	private SalesOrderResponse convertToSalesOrderResponse(SalesOrder salesOrder) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(salesOrder, SalesOrderResponse.class);
	}
	
	private SalesOrder convertToSalesOrder(SalesOrderRequest salesOrderRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(salesOrderRequest, SalesOrder.class);
	}
	
}
