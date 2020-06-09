package br.com.greenpay.core.system;

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

import br.com.greenpay.core.system.request.CustomerRequest;

@RestController
@Validated
//@RequestMapping("/")
@CrossOrigin
public class CustomerController {

	private static final Log LOGGER = LogFactory.getLog(CustomerController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	CustomerService customerService;

	@PostMapping(path = "/customers", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void create(@Valid @RequestBody CustomerRequest customerRequest) {
		customerService.create(customerRequest);
	}

	@GetMapping("/customers/{id}")
	public ResponseEntity<?> findCustomerById(@PathVariable("id") Long idCustomer) {
		return ResponseEntity.ok(customerService.findCustomerById(idCustomer));
	}
	
	@GetMapping("/customers_user/{id}")
	public ResponseEntity<?> findCustomerByIdUser(@PathVariable("id") String idUser) {
		return ResponseEntity.ok(customerService.findCustomerByIdUser(idUser));
	}

}
