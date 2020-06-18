package br.com.greenpay.core.system.response;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.greenpay.core.order.PaymentService;

@RestController
@Validated
@CrossOrigin
public class PaymentController {

	private static final Log LOGGER = LogFactory.getLog(PaymentController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PaymentService paymentService;
	
	@PostMapping(path = "/webhooks/payments", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createPartner(@Valid @RequestBody Codebeautify code) {
		System.out.println(code);
		return ResponseEntity.ok("created");
	}
	
	@GetMapping("/payments/{id}")
	public ResponseEntity<?> findOrderById(@PathVariable("id") String paymentId) {
		return ResponseEntity.ok(paymentService.listenerPayment(paymentId));
	}
	
}
