package br.com.greenpay.core.partner;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import br.com.greenpay.core.partner.request.CreatePlanRequest;
import br.com.greenpay.core.partner.request.PartnerRequest;
import br.com.greenpay.core.partner.response.PartnerResponse;
import br.com.greenpay.core.partner.response.PlanResponse;

@RestController
@Validated
@CrossOrigin
public class PartnerController {

	private static final Log LOGGER = LogFactory.getLog(PartnerController.class);

	@Autowired
	private PartnerService partnerService;

	@Autowired
	private HttpServletRequest request;

	@PostMapping(path = "/partners", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createPartner(@Valid @RequestBody PartnerRequest partnerRequest) {
		return ResponseEntity.ok(partnerService.createPartner(partnerRequest));
	}

	@GetMapping(path = "/partners", produces = "application/json")
	public List<PartnerResponse> findAllPartners() {
		return partnerService.findAllPartners();
	}

	@GetMapping("/partners/{id}")
	public ResponseEntity<?> findPartnerById(@PathVariable("id") Long partnerId) {
		return ResponseEntity.ok(partnerService.findPartnerById(partnerId));
	}

	@DeleteMapping("/partners/{id}")
	public void delete(@PathVariable(name = "id") Long partnerId) {
		partnerService.deleteById(partnerId);
	}

	@GetMapping("/partners_user/{id}")
	public ResponseEntity<?> findPartnerByIdUser(@PathVariable("id") String idUser) {
		return ResponseEntity.ok(partnerService.findPartnerByIdUser(idUser));
	}

	@GetMapping("/import_partner")
	public ResponseEntity<?> importPartner() {
		String size = partnerService.importPartner();
		Gson gson = new Gson();    
		return ResponseEntity.ok(gson.toJson(size));
	}

	@PutMapping("{id}/plans/{planId}")
	public ResponseEntity<?> createPlan(@Valid @RequestBody CreatePlanRequest createPlanReques, @PathVariable(name = "id") Long partnerId, @PathVariable(name = "planId") Long planId) {
		return ResponseEntity.ok(partnerService.createPlan(createPlanReques, partnerId, planId));
	}

	@GetMapping(path = "/plans", produces = "application/json")
	public List<PlanResponse> findAllPlansActive() {
		return partnerService.findAllPlansActive();
	}
	
}
