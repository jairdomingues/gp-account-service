package org.springframework.samples.petclinic.system;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

}
