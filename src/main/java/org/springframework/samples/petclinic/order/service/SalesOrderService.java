package org.springframework.samples.petclinic.order.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.order.controller.SalesOrderRequest;
import org.springframework.samples.petclinic.order.controller.SalesOrderResponse;
import org.springframework.samples.petclinic.order.model.SalesOrder;
import org.springframework.samples.petclinic.order.respository.SalesOrderRepository;
import org.springframework.samples.petclinic.system.Account;
import org.springframework.samples.petclinic.system.AccountResponse;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.samples.petclinic.system.Customer;
import org.springframework.samples.petclinic.system.Wallet;
import org.springframework.samples.petclinic.system.WalletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Service
@Transactional
public class SalesOrderService {

	private static final Log LOGGER = LogFactory.getLog(SalesOrderService.class);

	@Autowired
	private SalesOrderRepository salesOrderRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	public SalesOrderResponse createOrder(SalesOrderRequest salesOrderRequest) {
		SalesOrder salesOrder = this.convertToSalesOrder(salesOrderRequest);
		salesOrder.setStatus(SalesOrder.Status.PENDING);
		salesOrderRepository.save(salesOrder);
		if (paymentService.paymentSalesOrder(salesOrder.getId(), salesOrderRequest).equals("Approved")) {
			salesOrder.setStatus(SalesOrder.Status.PAID);
			salesOrderRepository.save(salesOrder);
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
