package org.springframework.samples.petclinic.system;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.order.PaymentService;
import org.springframework.samples.petclinic.system.model.TokenAccount.Type;
import org.springframework.samples.petclinic.system.request.CreditCardRequest;
import org.springframework.samples.petclinic.system.request.CurrentAccountRequest;
import org.springframework.samples.petclinic.system.request.DepositRequest;
import org.springframework.samples.petclinic.system.request.TokenAccountRequest;
import org.springframework.samples.petclinic.system.request.TokenAccountValidRequest;
import org.springframework.samples.petclinic.system.request.TransactionHistoryRequest;
import org.springframework.samples.petclinic.system.request.TransferRequest;
import org.springframework.samples.petclinic.system.request.WalletRequest;
import org.springframework.samples.petclinic.system.response.AccountResponse;
import org.springframework.samples.petclinic.system.response.MessageResponse;
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

	@Autowired
	PaymentService paymentService;

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
		paymentService.validTokenAccount(tokenAccountValidRequest);
		return ResponseEntity.ok(new MessageResponse("Payment approved!"));
	}

	@PostMapping(path = "/tansaction_history", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createTransactionHistory(@Valid @RequestBody TransactionHistoryRequest transactionHistoryRequest) {
		accountService.createTransactionHistory(transactionHistoryRequest);
		return ResponseEntity.ok(new MessageResponse("Transaction OK."));
	}

	@PostMapping(path = "/transfer", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createTransfer(@Valid @RequestBody TransferRequest transferRequest) {
		return ResponseEntity.ok(accountService.createTransfer(transferRequest));
	}

	@PostMapping(path = "/deposit", produces = "application/json", consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> createDeposit(@Valid @RequestBody DepositRequest depositRequest) {
		return ResponseEntity.ok(accountService.createDeposit(depositRequest));
	}

	@GetMapping(path = "/share/{id}", produces = "application/json")
	public ResponseEntity<?> shareToken(@PathVariable(name = "id") Long accountId) {
		TokenAccountRequest tokenAccountRequest = new TokenAccountRequest();
		tokenAccountRequest.setAccountId(accountId);
		tokenAccountRequest.setType(Type.SHARE);
		return ResponseEntity.ok(accountService.genereateToken(tokenAccountRequest));
	}
	
	@GetMapping(path = "/accounts", produces = "application/json")
	public List<AccountResponse> findAllAccounts() {
		return accountService.findAllAccounts();
	}

	@GetMapping(path = "/customer_accounts/{id}", produces = "application/json")
	public List<AccountResponse> findAllByCustomer(@PathVariable(name = "id") Long customerId) {
		return accountService.findAllByCustomer(customerId);
	}
	
	@GetMapping(path = "/cpf_accounts/{cpf}", produces = "application/json")
	public List<AccountResponse> findAllByCpf(@PathVariable(name = "cpf") String cpf) {
		return accountService.findAllByCpf(cpf);
	}

	@DeleteMapping("/accounts/{id}")
	public void delete(@PathVariable(name = "id") Long idAccount) {
		accountService.deleteById(idAccount);
	}

}
