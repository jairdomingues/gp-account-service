package org.springframework.samples.petclinic.order;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.system.Account;
import org.springframework.samples.petclinic.system.AccountService;
import org.springframework.samples.petclinic.system.CurrentAccount;
import org.springframework.samples.petclinic.system.CustomGenericNotFoundException;
import org.springframework.samples.petclinic.system.Customer;
import org.springframework.samples.petclinic.system.CustomerRepository;
import org.springframework.samples.petclinic.system.LoginAppRequest;
import org.springframework.samples.petclinic.system.LoginAppResponse;
import org.springframework.samples.petclinic.system.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.TransactionHistory.TransactionType;
import org.springframework.samples.petclinic.system.Wallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class PaymentService {

	private static final Log LOGGER = LogFactory.getLog(PaymentService.class);

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AccountService accountService;
	
	public String paymentSalesOrder(Long orderId, SalesOrderRequest salesOrderRequest) {

		Customer customer = customerRepository.findById(salesOrderRequest.getClientRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		//processa os metodos do pagamento informados
		for (PaymentRequest paymentRequest: salesOrderRequest.getPayments()) {
			//pagamento com crypto moedas
			if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CryptoCurrency)) {
				for ( Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof Wallet) {
						Wallet wallet = (Wallet) account;
						process(wallet.getHashCard(), paymentRequest.getAmount().toString());
						accountService.createTransactionHistory(wallet.getId(), Operation.PAYMENT, TransactionType.DEBIT,
								"Pagto com crypto moeda id " + orderId,
								paymentRequest.getAmount(), 1l);
					}
				}
			//pagamento com conta digital
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CurrentAccount)) {
				for ( Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof CurrentAccount) {
						CurrentAccount currentAccount = (CurrentAccount) account;
						accountService.createTransactionHistory(currentAccount.getId(), Operation.PAYMENT, TransactionType.DEBIT,
								"Pagto conta digital id " + orderId,
								paymentRequest.getAmount(), 1l);
					}
				}
			//pagamento com cartao de credito
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CreditCard)) {

			}	
		}
		return "Approved";
		
	}

	//processar pagamentos com crypto moedas
	//TODO ir para microservice especializado
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
			final String url = "https://treeppayhmg.azurewebsites.net/api/Venda/VendaDireta?hashCartao="+hashCard+"&valor="+amount;
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
	
	
}
