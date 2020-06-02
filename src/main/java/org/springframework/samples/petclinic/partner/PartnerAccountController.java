package org.springframework.samples.petclinic.partner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@CrossOrigin
public class PartnerAccountController {

	private static final Log LOGGER = LogFactory.getLog(PartnerAccountController.class);

	@Autowired
	private PartnerAccountService partnerAccountService;

	@Autowired
	private HttpServletRequest request;

	@GetMapping("/partner_accounts/{id}")
	public ResponseEntity<?> findPartnerAccountByPartner(@PathVariable("id") Long partnerId) {
		return ResponseEntity.ok(partnerAccountService.findPartnerAccountByPartner(partnerId));
	}

}
