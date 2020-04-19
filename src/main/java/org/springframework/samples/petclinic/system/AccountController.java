package org.springframework.samples.petclinic.system;

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
//@RequestMapping("/")
@CrossOrigin
public class AccountController {

	private static final Log LOGGER = LogFactory.getLog(AccountController.class);

	@Autowired
	private HttpServletRequest request;

	@Autowired
	AccountService accountService;

	@PostMapping(path = "/current_account", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createCurrentAccount(@Valid @RequestBody CurrentAccountRequest curreantAccountRequest) {
		accountService.createCurrentAccount(curreantAccountRequest);
	}

	@PostMapping(path = "/credit_card", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createCreditCard(@Valid @RequestBody CreditCardRequest creditCardRequest) {
		accountService.createCreditCard(creditCardRequest);
	}

	@PostMapping(path = "/wallet", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public void createWallet(@Valid @RequestBody WalletRequest walletRequest) {
		accountService.createWallet(walletRequest);
	}

	@PostMapping(path = "/token_account", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> genereateToken(@Valid @RequestBody TokenAccountRequest tokenAccountRequest) {
		return ResponseEntity.ok(accountService.genereateToken(tokenAccountRequest));
	}

	@PostMapping(path = "/valid_token", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> validToken(@Valid @RequestBody TokenAccountValidRequest tokenAccountValidRequest) {
		accountService.validTokenAccount(tokenAccountValidRequest);
		return ResponseEntity.ok(new MessageResponse("Payment approved!"));
	}

	@GetMapping(path = "/accounts", produces = "application/json")
	public List<AccountResponse> findAllAccounts() {
		return accountService.findAllAccounts();
	}

	@DeleteMapping("/accounts/{id}")
	public void delete(@PathVariable(name = "id") Long idAccount) {
		accountService.deleteById(idAccount);
	}

}
