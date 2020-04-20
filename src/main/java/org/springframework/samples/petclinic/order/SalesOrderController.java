package org.springframework.samples.petclinic.order;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@CrossOrigin
public class SalesOrderController {

	private static final Log LOGGER = LogFactory.getLog(SalesOrderController.class);

	@Autowired
	private SalesOrderService salesOrderService;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(path = "/orders", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createOrder(@Valid @RequestBody SalesOrderRequest salesOrderRequest) {
		return ResponseEntity.ok(salesOrderService.createOrder(salesOrderRequest));
	}

	@GetMapping(path = "/orders", produces = "application/json")
	public List<SalesOrderResponse> findAllOrders() {
		return salesOrderService.findAllOrders();
	}

	@GetMapping("/orders/{id}")
	public ResponseEntity<?> findOrderById(@PathVariable("id") Long orderId) {
		return ResponseEntity.ok(salesOrderService.findOrderById(orderId));
	}

	@DeleteMapping("/orders/{id}")
	public void delete(@PathVariable(name = "id") Long orderId) {
		salesOrderService.deleteById(orderId);
	}

}
