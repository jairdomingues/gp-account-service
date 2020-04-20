package org.springframework.samples.petclinic.order;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
