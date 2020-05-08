package org.springframework.samples.petclinic.order;

import java.math.BigDecimal;
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
import org.springframework.samples.petclinic.partner.ReleaseHistory;
import org.springframework.samples.petclinic.system.Account;
import org.springframework.samples.petclinic.system.AccountRepository;
import org.springframework.samples.petclinic.system.AccountResponse;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.moip.API;
import br.com.moip.Client;
import br.com.moip.authentication.Authentication;
import br.com.moip.authentication.BasicAuth;
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.request.CreditCardRequest;
import br.com.moip.request.FundingInstrumentRequest;
import br.com.moip.request.HolderRequest;
import br.com.moip.request.PhoneRequest;
import br.com.moip.request.TaxDocumentRequest;

@Service
@Transactional
public class PaymentService {

	private static final Log LOGGER = LogFactory.getLog(PaymentService.class);
	
	private static final BigDecimal PAYMENT_FEE_CRYPTO = new BigDecimal(2.5);
	private static final BigDecimal PAYMENT_FEE_ACCOUNT = new BigDecimal(1.0);
	private static final BigDecimal PAYMENT_FEE_CREDIT = new BigDecimal(2.5);

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

		Boolean paymentCrypto = false;
		Boolean paymentAccount = false;
		
		ResultPaymentResponse resultPaymentResponse = new ResultPaymentResponse();
		// processa os metodos do pagamento informados
		for (PaymentRequest paymentRequest : salesOrderRequest.getPayments()) {
			//pagamento com conta digital
			if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CURRENT_ACCOUNT)) {
				//debito na conta digital do customer
				
				Account account = accountRepository.findById(paymentRequest.getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));
				if (account instanceof CurrentAccount) {
					CurrentAccount currentAccount = (CurrentAccount) account;
					accountService.createTransactionHistory(currentAccount.getId(), Operation.PAYMENT,
							TransactionType.DEBIT, Status.ACTIVE, "Pagamento conta digital Id " + orderId,
							paymentRequest.getAmount(), orderId);
				}
				
				//calculo taxa
				BigDecimal paymentFee = (paymentRequest.getAmount().multiply(PAYMENT_FEE_ACCOUNT)).divide(new BigDecimal(100));

				//credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.SALES,
						ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE, "Recebimento venda pedido Id " + orderId,
						paymentRequest.getAmount(), orderId);
				
				//credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.PAYMENT_FEE,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE, "Pagamento taxa pedido Id " + orderId,
						paymentFee, orderId);

				resultPaymentResponse.setTransactionAccount("APPROVED");
				paymentAccount = true;
				
			//pagamento com cartao de credito
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CREDIT_CARD)) {

				this.moip();
				
			//pagamento com crypto moedas
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.WALLET)) {
				for (Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof Wallet) {
						Wallet wallet = (Wallet) account;
						LoginAppResponse loginAppResponse = this.process(wallet.getHashCard(), paymentRequest.getAmount().toString());

						//credito transferencia carteira cliente
						accountService.createTransactionHistory(wallet.getId(), Operation.TRANSFER,
								TransactionType.CREDIT, Status.ACTIVE, "Transferẽncia carteira crypto Id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), orderId);

						//debito com pagamento crypto moeda
						accountService.createTransactionHistory(wallet.getId(), Operation.PAYMENT,
								TransactionType.DEBIT, Status.ACTIVE, "Pagamento crypto moeda Id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), orderId);
						resultPaymentResponse.setTransactionCrypto(loginAppResponse.getResultado());
					}
				}
				
				//credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.SALES,
						ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE, "Venda pedido Id " + orderId,
						paymentRequest.getAmount(), orderId);

				//calculo taxa
				BigDecimal paymentFee = (paymentRequest.getAmount().multiply(PAYMENT_FEE_CRYPTO)).divide(new BigDecimal(100));
				
				//debito valor liquido na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.TRANSFER,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE, "Transferẽncia venda pedido Id " + orderId,
						paymentRequest.getAmount().subtract(paymentFee), orderId);
				
				//debito taxas na conta digital do partner
				partnerAccountService.createReleaseHistory(partner.getId(), ReleaseHistory.Operation.PAYMENT_FEE,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE, "Pagamento taxa pedido Id " + orderId, paymentFee, orderId);
				
				paymentCrypto = true;

			}
		}
		//verificar aqui importnante tratar as formas
//		if ((resultPaymentResponse.getTransactionAccount()!=null&&resultPaymentResponse.getTransactionAccount().equalsIgnoreCase("APPROVED")) || resultPaymentResponse.getTransactionCrypto()!=null) {
		if (paymentAccount) {
			resultPaymentResponse.setStatus("OK");
		}
		
//		}
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

		if (this.paymentSalesOrder(tokenAccountValidRequest.getOrderId(), salesOrderRequest).getStatus().equalsIgnoreCase("OK")) {
			salesOrder.setStatus(SalesOrder.Status.PAID);
		} else {
			salesOrder.setStatus(SalesOrder.Status.CANCELED);
		}
		salesOrder.setClientRef(account.getCustomer().getId());
		salesOrderRepository.save(salesOrder);
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
		tokenAccount.setValid(true);
		tokenAccountRepository.save(tokenAccount);
//		if (tokenAccount.getExpired()) {
//			throw new CustomGenericNotFoundException("Error: Token is expired.");
//		}
		return tokenAccount;
	}
	
	
	 // Basic Auth
    private final String token = "A4AALFS3JAPUSDE5RJ1VTP1LENMT5KOQ";
    private final String key = "JEW5FQ0ZV5MJUQWRLEXTYH0F843KDJOPW8XGZE0W";

    // OAuth
    private final String oauth = "QTRBQUxGUzNKQVBVU0RFNVJKMVZUUDFMRU5NVDVLT1E6SkVXNUZRMFpWNU1KVVFXUkxFWFRZSDBGODQzS0RKT1BXOFhHWkUwVw==";

    private API buildSetup() {

        // Set Authentication
        //Authentication auth = new OAuth(oauth);
        Authentication auth = new BasicAuth(token, key);
        
        // Set Client
        Client client = new Client(Client.SANDBOX, auth);

        // Instantiate API
        API api = new API(client);

        return api;
    }
    
    private void moip() {
    	
    	  API api = this.buildSetup();
    	  
    	  try {    	  
    	  br.com.moip.resource.Payment createdPayment = api.payment().create(new br.com.moip.request.PaymentRequest()
    	            .orderId("ORD-XF2jair9LOEE180J")        // Order's Moip ID
    	            .installmentCount(1)
    	            .fundingInstrument(new FundingInstrumentRequest()
    	                .creditCard(new CreditCardRequest()
    	                    .number("5555666677778884")
    	                    .cvc(123)
    	                    .expirationMonth("06")
    	                    .expirationYear("22")
    	                    .holder(new HolderRequest()
    	                        .fullname("Jose Portador da Silva")
    	                        .birthdate("1988-10-10")
    	                            .phone(new PhoneRequest()
    	                            .setAreaCode("11")
    	                            .setNumber("55667788")
    	                        )
    	                        .taxDocument(TaxDocumentRequest.cpf("22222222222"))
    	                    )
    	                    .store(true)
    	                )
    	            )
    	        );

    	  System.out.println(createdPayment);

	  	} catch(UnauthorizedException e) {
    	  System.out.println(e.getMessage());
		} catch(UnexpectedException e) {
    	  System.out.println(e.getMessage());
		} catch(ValidationException e) {
    	  System.out.println(e.getMessage());
		}    	  
    	
    }

}
