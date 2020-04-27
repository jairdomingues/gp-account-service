package org.springframework.samples.petclinic.order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.partner.Partner;
import org.springframework.samples.petclinic.partner.PartnerAccountService;
import org.springframework.samples.petclinic.partner.PartnerRepository;
import org.springframework.samples.petclinic.system.Account;
import org.springframework.samples.petclinic.system.AccountRepository;
import org.springframework.samples.petclinic.system.AccountService;
import org.springframework.samples.petclinic.system.CurrentAccount;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.samples.petclinic.system.Customer;
import org.springframework.samples.petclinic.system.CustomerRepository;
import org.springframework.samples.petclinic.system.LoginAppRequest;
import org.springframework.samples.petclinic.system.LoginAppResponse;
import org.springframework.samples.petclinic.system.TokenAccount;
import org.springframework.samples.petclinic.system.TokenAccountRepository;
import org.springframework.samples.petclinic.system.TokenAccountValidRequest;
import org.springframework.samples.petclinic.system.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.TransactionHistory.Status;
import org.springframework.samples.petclinic.system.TransactionHistory.TransactionType;
import org.springframework.samples.petclinic.system.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.samples.petclinic.partner.ReleaseHistory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class PaymentService {

	private static final Log LOGGER = LogFactory.getLog(PaymentService.class);

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	SalesOrderRepository salesOrderRepository;

	@Autowired
	TokenAccountRepository tokenAccountRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;

	@Autowired
	PartnerAccountService partnerAccountService;

	@Autowired
	PartnerRepository partnerRepository;

	public ResultPaymentResponse paymentSalesOrder(Long orderId, SalesOrderRequest salesOrderRequest) {

		Customer customer = customerRepository.findById(salesOrderRequest.getClientRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		Partner partner = partnerRepository.findById(salesOrderRequest.getPartnerRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));

		ResultPaymentResponse resultPaymentResponse = new ResultPaymentResponse();
		// processa os metodos do pagamento informados
		for (PaymentRequest paymentRequest : salesOrderRequest.getPayments()) {
			//pagamento com conta digital
			if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CurrentAccount)) {
				//debito na conta digital do customer
				for (Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof CurrentAccount) {
						CurrentAccount currentAccount = (CurrentAccount) account;
						accountService.createTransactionHistory(currentAccount.getId(), Operation.PAYMENT,
								TransactionType.DEBIT, Status.ACTIVE, "Pagto conta digital id " + orderId,
								paymentRequest.getAmount(), 1l);
					}
				}
				//credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.SALES,
						ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE, "Recbto venda pedido id " + orderId,
						paymentRequest.getAmount(), 1l);
				
				resultPaymentResponse.setTransactionAccount("APPROVED");
			//pagamento com cartao de credito
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CreditCard)) {

				
			//pagamento com crypto moedas
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CryptoCurrency)) {
				for (Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof Wallet) {
						Wallet wallet = (Wallet) account;
						LoginAppResponse loginAppResponse = this.process(wallet.getHashCard(), paymentRequest.getAmount().toString());
						//credito transferencia carteira cliente
						accountService.createTransactionHistory(wallet.getId(), Operation.TRANSFER,
								TransactionType.CREDIT, Status.ACTIVE, "Recbto carteira crypto id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), 1l);
						//debito com pagamento crypto moeda
						accountService.createTransactionHistory(wallet.getId(), Operation.PAYMENT,
								TransactionType.DEBIT, Status.ACTIVE, "Pagto com crypto moeda id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), 1l);
						resultPaymentResponse.setTransactionCrypto(loginAppResponse.getResultado());
					}
				}
			}
		}
		if ((resultPaymentResponse.getTransactionAccount()!=null&&resultPaymentResponse.getTransactionAccount().equalsIgnoreCase("APPROVED")) || resultPaymentResponse.getTransactionCrypto()!=null) {
			resultPaymentResponse.setStatus("OK");
		}
		return resultPaymentResponse;

	}

	public void validTokenAccount(TokenAccountValidRequest tokenAccountValidRequest) {

		SalesOrder salesOrder = salesOrderRepository.findById(tokenAccountValidRequest.getOrderId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: SalesOrder is not found."));

		TokenAccount tokenAccount = this.updateToken(tokenAccountValidRequest.getUuid());
		
		Account account = accountRepository.findById(tokenAccount.getAccount().getId())
			.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));
		
		SalesOrderRequest salesOrderRequest = new SalesOrderRequest();
		salesOrderRequest.setClientRef(account.getCustomer().getId());
		salesOrderRequest.setPartnerRef(salesOrder.getPartnerRef());
		List<PaymentRequest> payments = salesOrder.getPayments().stream().map(this::convertToPaymentRequest)
				.collect(Collectors.toList());
		salesOrderRequest.setPayments(payments);

		if (this.paymentSalesOrder(tokenAccountValidRequest.getOrderId(), salesOrderRequest).equals("Approved")) {
			salesOrder.setStatus(SalesOrder.Status.PAID);
			salesOrderRepository.save(salesOrder);
		}
	}

	private PaymentRequest convertToPaymentRequest(Payment payment) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(payment, PaymentRequest.class);
	}

	// processar pagamentos com crypto moedas
	// TODO ir para microservice especializado
	private LoginAppResponse process(String hashCard, String amount) {

		final String uri = "https://treeppayhmg.azurewebsites.net/api/Conta/loginApp";
		ResponseEntity<LoginAppResponse> response = null;
		LoginAppRequest loginApp = new LoginAppRequest();
		loginApp.setLogin("teste@teste.com");
		loginApp.setSenha("123456");
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<LoginAppRequest> entity = new HttpEntity<LoginAppRequest>(loginApp, headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<LoginAppResponse> result = restTemplate.postForEntity(uri, entity, LoginAppResponse.class);
		if (result.getBody().getSucesso().equals("true")) {
			String token = result.getBody().getResultado();
			final String url = "https://treeppayhmg.azurewebsites.net/api/Venda/VendaDireta?hashCartao=" + hashCard
					+ "&valor=" + amount;
			headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
			Map<String, String> bodyParamMap = new HashMap<String, String>();
			bodyParamMap.put("hashCartao", "BF0440CB-1857-40F5-9F60-DA18899B3883");
			bodyParamMap.put("valor", "1");
			String reqBodyData = null;
			try {
				reqBodyData = new ObjectMapper().writeValueAsString(bodyParamMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			HttpEntity<String> requestEnty = new HttpEntity<>(reqBodyData, headers);
			restTemplate = new RestTemplate();
			response = restTemplate.postForEntity(url, requestEnty, LoginAppResponse.class);
			if (!response.getBody().getSucesso().equals("true")) {
				throw new CustomGenericNotFoundException("Error: not authorized");
			}
		} else {
			throw new CustomGenericNotFoundException("Error: not authorized");
		}
		return response.getBody();
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	private TokenAccount updateToken(String uuid) {
		TokenAccount tokenAccount = tokenAccountRepository.findByUuidValid(uuid)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Token is invalid."));
		tokenAccount.setValid(false);
		tokenAccountRepository.save(tokenAccount);
//		if (tokenAccount.getExpired()) {
//			throw new CustomGenericNotFoundException("Error: Token is expired.");
//		}
		return tokenAccount;
	}

}
