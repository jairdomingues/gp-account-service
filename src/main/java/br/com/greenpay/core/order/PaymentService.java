package br.com.greenpay.core.order;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.greenpay.core.order.model.AccountWireCard;
import br.com.greenpay.core.order.model.Payment;
import br.com.greenpay.core.order.model.PaymentWireCard;
import br.com.greenpay.core.order.model.SalesOrder;
import br.com.greenpay.core.order.repository.AccountWireCardRepository;
import br.com.greenpay.core.order.repository.SalesOrderRepository;
import br.com.greenpay.core.order.request.PaymentRequest;
import br.com.greenpay.core.order.request.SalesOrderRequest;
import br.com.greenpay.core.order.response.ResultPaymentResponse;
import br.com.greenpay.core.partner.PartnerAccount;
import br.com.greenpay.core.partner.PartnerAccountService;
import br.com.greenpay.core.partner.model.Partner;
import br.com.greenpay.core.partner.model.Plan;
import br.com.greenpay.core.partner.model.ReleaseHistory;
import br.com.greenpay.core.partner.repository.PartnerAccountRepository;
import br.com.greenpay.core.partner.repository.PartnerRepository;
import br.com.greenpay.core.partner.request.CardRequest;
import br.com.greenpay.core.partner.request.CreatePlanRequest;
import br.com.greenpay.core.system.AccountService;
import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;
import br.com.greenpay.core.system.exception.CustomGenericUnauthorizedException;
import br.com.greenpay.core.system.model.Account;
import br.com.greenpay.core.system.model.CreditCard;
import br.com.greenpay.core.system.model.CurrentAccount;
import br.com.greenpay.core.system.model.Customer;
import br.com.greenpay.core.system.model.TokenAccount;
import br.com.greenpay.core.system.model.Wallet;
import br.com.greenpay.core.system.model.TransactionHistory.Operation;
import br.com.greenpay.core.system.model.TransactionHistory.Status;
import br.com.greenpay.core.system.model.TransactionHistory.TransactionType;
import br.com.greenpay.core.system.repository.AccountRepository;
import br.com.greenpay.core.system.repository.CustomerRepository;
import br.com.greenpay.core.system.repository.TokenAccountRepository;
import br.com.greenpay.core.system.request.LoginAppRequest;
import br.com.greenpay.core.system.request.TokenAccountValidRequest;
import br.com.greenpay.core.system.response.LoginAppResponse;
import br.com.greenpay.core.wirecard.AccountWireCardRequest;
import br.com.greenpay.core.wirecard.AccountWireCardRequest.Type;
import br.com.moip.API;
import br.com.moip.Client;
import br.com.moip.authentication.Authentication;
import br.com.moip.authentication.BasicAuth;
import br.com.moip.authentication.OAuth;
import br.com.moip.exception.UnauthorizedException;
import br.com.moip.exception.UnexpectedException;
import br.com.moip.exception.ValidationException;
import br.com.moip.request.AccountRequest;
import br.com.moip.request.AmountRequest;
import br.com.moip.request.ApiDateRequest;
import br.com.moip.request.BoletoRequest;
import br.com.moip.request.CreditCardRequest;
import br.com.moip.request.CustomerRequest;
import br.com.moip.request.FundingInstrumentRequest;
import br.com.moip.request.HolderRequest;
import br.com.moip.request.IdentityDocumentRequest;
import br.com.moip.request.InstructionLinesRequest;
import br.com.moip.request.OrderAmountRequest;
import br.com.moip.request.OrderRequest;
import br.com.moip.request.PersonRequest;
import br.com.moip.request.PhoneRequest;
import br.com.moip.request.ReceiverRequest;
import br.com.moip.request.ShippingAddressRequest;
import br.com.moip.request.SubtotalsRequest;
import br.com.moip.request.TaxDocumentRequest;
import br.com.moip.resource.Order;

@Service
@Transactional
public class PaymentService {

	private static final Log LOGGER = LogFactory.getLog(PaymentService.class);

	private static final BigDecimal PAYMENT_FEE_CRYPTO = new BigDecimal(2.5);
	private static final BigDecimal PAYMENT_FEE_ACCOUNT = new BigDecimal(1.0);
	private static final BigDecimal PAYMENT_FEE_CREDIT = new BigDecimal(2.5);
	private static final BigDecimal PAYMENT_FEE_AKI_1 = new BigDecimal(0.08);
	private static final BigDecimal PAYMENT_FEE_AKI_2 = new BigDecimal(0.13);
	private static final BigDecimal PAYMENT_FEE_GREEN_1 = new BigDecimal(0.8);
	private static final BigDecimal PAYMENT_FEE_GREEN_2 = new BigDecimal(1.3);
	private static final BigDecimal PAYMENT_FEE_MEMBER_SHIP_GREEN = new BigDecimal(97.0);

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
	PartnerAccountRepository partnerAccountRepository;

	@Autowired
	PartnerRepository partnerRepository;

	@Autowired
	private AccountWireCardRepository accountWireCardRepository;

	public ResultPaymentResponse paymentSalesOrder(Long orderId, SalesOrderRequest salesOrderRequest) {

		Customer customer = customerRepository.findById(salesOrderRequest.getClientRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		Partner partner = partnerRepository.findById(salesOrderRequest.getPartnerRef())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));

		Boolean paymentCrypto = false;
		Boolean paymentAccount = false;
		Boolean paymentCredit = false;

		ResultPaymentResponse resultPaymentResponse = new ResultPaymentResponse();
		// processa os metodos do pagamento informados
		for (PaymentRequest paymentRequest : salesOrderRequest.getPayments()) {
			// pagamento com conta digital
			if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CURRENT_ACCOUNT)) {
				// debito na conta digital do customer

				Account account = accountRepository.findById(paymentRequest.getId())
						.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));
				if (account instanceof CurrentAccount) {
					CurrentAccount currentAccount = (CurrentAccount) account;
					accountService.createTransactionHistory(currentAccount.getId(), Operation.PAYMENT,
							TransactionType.DEBIT, Status.ACTIVE, "Pagamento conta digital Id " + orderId,
							paymentRequest.getAmount(), orderId);
				}

				// calculo taxa
				BigDecimal paymentFee = (paymentRequest.getAmount().multiply(PAYMENT_FEE_ACCOUNT))
						.divide(new BigDecimal(100));

				PartnerAccount partnerAccount = partnerAccountRepository
						.findPartnerAccountByPartner(salesOrderRequest.getPartnerRef());

				// credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.SALES,
						ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE,
						"Recebimento venda pedido Id " + orderId, paymentRequest.getAmount(), orderId);

				// credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.PAYMENT_FEE,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE,
						"Pagamento taxa pedido Id " + orderId, paymentFee, orderId);

				paymentAccount = true;

				// pagamento com cartao de credito
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.CREDIT_CARD)) {

				Account account = accountRepository.findById(paymentRequest.getId())
						.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));

				CardRequest card = new CardRequest();
				CreditCard creditCard = (CreditCard) account;
				card.setBrand(creditCard.getBrand());
				card.setCardholderName(creditCard.getCardholderName());
				card.setCardNumber(creditCard.getCardNumber());
				card.setExpirationMonth(creditCard.getExpirationMonth());
				card.setExpirationYear(creditCard.getExpirationYear());
				card.setSecurityCode(creditCard.getSecurityCode());

				AccountWireCardRequest accountWireCardRequest = new AccountWireCardRequest();
				accountWireCardRequest.setType(Type.CREDIT);
				accountWireCardRequest.setEmail("jairsyonet@gmail.com");
				accountWireCardRequest.setName(customer.getFirstname());
				accountWireCardRequest.setLastName(customer.getLastname());
				accountWireCardRequest.setCpf(customer.getDocument());
				accountWireCardRequest.setStreet(customer.getAdresses().stream().findFirst().get().getAddress());
				accountWireCardRequest.setStreetNumber(customer.getAdresses().stream().findFirst().get().getNumber());
				accountWireCardRequest.setDistrict(customer.getAdresses().stream().findFirst().get().getNeighborhood());
				accountWireCardRequest.setZipCode(customer.getAdresses().stream().findFirst().get().getZip());
				accountWireCardRequest.setCity(customer.getAdresses().stream().findFirst().get().getCity());
				accountWireCardRequest.setState(customer.getAdresses().stream().findFirst().get().getProvince());
				accountWireCardRequest.setCountry("BRA");
				accountWireCardRequest.setCountryCode("55");
				accountWireCardRequest.setAreaCode("51");
				accountWireCardRequest.setNumber("99090909");
				accountWireCardRequest.setProduct("produto A");
				// transforma em centavos
				String value = paymentRequest.getAmount().toString().replace(".", "");
				accountWireCardRequest.setValue(value);

				PaymentWireCard paymentWireCard = this.moipCard(accountWireCardRequest, card);
				paymentAccount = true;

				// pagamento com crypto moedas
			} else if (paymentRequest.getPaymentMethod().equals(Payment.PaymentMethod.WALLET)) {
				for (Account account : customer.getWalletOfCustomer().getAccountsInWallet()) {
					if (account instanceof Wallet) {
						Wallet wallet = (Wallet) account;
						LoginAppResponse loginAppResponse = this.process(wallet.getHashCard(),
								paymentRequest.getAmount().toString());

						// credito transferencia carteira cliente
						accountService.createTransactionHistory(wallet.getId(), Operation.TRANSFER,
								TransactionType.CREDIT, Status.ACTIVE,
								"Transferẽncia carteira crypto Id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), orderId);

						// debito com pagamento crypto moeda
						accountService.createTransactionHistory(wallet.getId(), Operation.PAYMENT,
								TransactionType.DEBIT, Status.ACTIVE,
								"Pagamento crypto moeda Id " + loginAppResponse.getResultado(),
								paymentRequest.getAmount(), orderId);
						resultPaymentResponse.setTransactionCrypto(loginAppResponse.getResultado());
					}
				}

				PartnerAccount partnerAccount = partnerAccountRepository
						.findPartnerAccountByPartner(salesOrderRequest.getPartnerRef());

				// credito na conta digital do partner
				partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.SALES,
						ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE,
						"Venda pedido Id " + orderId, paymentRequest.getAmount(), orderId);

				// calculo taxa
				BigDecimal paymentFee = (paymentRequest.getAmount().multiply(PAYMENT_FEE_CRYPTO))
						.divide(new BigDecimal(100));

				// debito valor liquido na conta digital do partner
				partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.TRANSFER,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE,
						"Transferẽncia venda pedido Id " + orderId, paymentRequest.getAmount().subtract(paymentFee),
						orderId);

				// debito taxas na conta digital do partner
				partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.PAYMENT_FEE,
						ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE,
						"Pagamento taxa pedido Id " + orderId, paymentFee, orderId);
				paymentCrypto = true;

			}
		}
		// verificar aqui importnante tratar as formas
		if (paymentAccount || paymentCredit || paymentCrypto) {
			resultPaymentResponse.setStatus("OK");
		}
		return resultPaymentResponse;

	}

	public void validTokenAccount(TokenAccountValidRequest tokenAccountValidRequest) {

		SalesOrder salesOrder = salesOrderRepository.findById(tokenAccountValidRequest.getOrderId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: SalesOrder is not found."));

		TokenAccount tokenAccount = accountService.updateToken(tokenAccountValidRequest.getUuid());

		Account account = accountRepository.findById(tokenAccount.getAccount().getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));

		SalesOrderRequest salesOrderRequest = new SalesOrderRequest();
		salesOrderRequest.setClientRef(account.getCustomer().getId());
		salesOrderRequest.setPartnerRef(salesOrder.getPartnerRef());
		List<PaymentRequest> payments = salesOrder.getPayments().stream().map(this::convertToPaymentRequest)
				.collect(Collectors.toList());
		salesOrderRequest.setPayments(payments);

		if (this.paymentSalesOrder(tokenAccountValidRequest.getOrderId(), salesOrderRequest).getStatus()
				.equalsIgnoreCase("OK")) {
			salesOrder.setStatus(SalesOrder.Status.PAID);
		} else {
			salesOrder.setStatus(SalesOrder.Status.CANCELED);
		}
		salesOrder.setClientRef(account.getCustomer().getId());
		salesOrderRepository.save(salesOrder);
	}

	public void accountWireCard(Partner partner, Plan plan, PartnerAccount partnerAccount,
			CreatePlanRequest createPlanReques) {

		// criar wirecard para conta transparente do partner
		AccountWireCardRequest accountWireCardRequest = new AccountWireCardRequest();
		accountWireCardRequest.setType(Type.MEMBER_FEE);
		accountWireCardRequest.setEmail(createPlanReques.getContact().getEmail());
		accountWireCardRequest.setName(createPlanReques.getContact().getFirstname());
		accountWireCardRequest.setLastName(createPlanReques.getContact().getLastname());
		accountWireCardRequest.setCpf(createPlanReques.getContact().getCpf());
		accountWireCardRequest.setStreet(partner.getAdresses().stream().findFirst().get().getAddress());
		accountWireCardRequest.setStreetNumber(partner.getAdresses().stream().findFirst().get().getNumber());
		accountWireCardRequest.setDistrict(partner.getAdresses().stream().findFirst().get().getNeighborhood());
		accountWireCardRequest.setZipCode(partner.getAdresses().stream().findFirst().get().getZip());
		accountWireCardRequest.setCity(partner.getAdresses().stream().findFirst().get().getCity());
		accountWireCardRequest.setState(partner.getAdresses().stream().findFirst().get().getProvince());
		accountWireCardRequest.setCountry("BRA");
		accountWireCardRequest.setCountryCode("55");
		accountWireCardRequest.setAreaCode("51");
		accountWireCardRequest.setNumber("99090909");
		accountWireCardRequest.setProduct(plan.getName());
		// transforma em centavos para api da wirecard
		String value = plan.getMemberFee().toString().replace(".", "");
		accountWireCardRequest.setValue(value);
		//criar conta transparente para wirecard
		br.com.moip.resource.Account account = this.createAccountWireCard(accountWireCardRequest);

		//salvar dados da conta transparente criada para wirecard
		AccountWireCard accountWireCard = new AccountWireCard();
		accountWireCard.set_id(account.getId());
		accountWireCard.setAccessToken(account.getAccessToken());
		accountWireCard.setEmail(account.getEmail().getAddress());
		accountWireCard.setLogin(account.getLogin());
		accountWireCard.setPartner(partner);
		accountWireCard.setTransparentAccount(true);
		accountWireCardRepository.save(accountWireCard);

		//efetuar o pagamento cartão de crédito
		accountWireCardRequest.setMoipAccount(account.getId());
		PaymentWireCard paymentWireCard = this.moipCard(accountWireCardRequest, createPlanReques.getCard());
		paymentWireCard.setPartner(partner);
		accountWireCardRepository.save(accountWireCard);

		partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.PAYMENT,
				ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE, "Lançamento adesão",
				plan.getMemberFee(), 1l);

		partnerAccountService.createReleaseHistory(partnerAccount.getId(), ReleaseHistory.Operation.TRANSFER,
				ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE, "Pagamento adesão",
				plan.getMemberFee(), 1l);

		PartnerAccount partnerAccountAdmin = partnerAccountRepository.findById(1l)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner Admin is not found."));

		// credito na conta digital do partner admin
//		partnerAccountService.createReleaseHistory(partnerAccountAdmin.getId(), ReleaseHistory.Operation.SALES,
//				ReleaseHistory.TransactionType.CREDIT, ReleaseHistory.Status.ACTIVE,
//				"Recebimento de adesão ", new BigDecimal(payment.), 1l);
//	
//		// credito na conta digital do partner admin
//		partnerAccountService.createReleaseHistory(partnerAccountAdmin.getId(), ReleaseHistory.Operation.PAYMENT_FEE,
//				ReleaseHistory.TransactionType.DEBIT, ReleaseHistory.Status.ACTIVE,
//				"Pagamento taxa adesão ", new BigDecimal(accountWireCardRequest.getValue()), 1l);

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

	// Basic Auth
	private final String token = "A4AALFS3JAPUSDE5RJ1VTP1LENMT5KOQ";
	private final String key = "JEW5FQ0ZV5MJUQWRLEXTYH0F843KDJOPW8XGZE0W";

	// OAuth
	private final String oauth = "5d64a91d276443a7a2befab13156e23b_v2";

	private API buildSetup() {

		// Set Authentication
		// Authentication auth = new OAuth(oauth);
		Authentication auth1 = new BasicAuth(token, key);

		Authentication auth = new OAuth("5d64a91d276443a7a2befab13156e23b_v2");

		// Set Client
		Client client = new Client(Client.SANDBOX, auth);

		// Instantiate API
		API api = new API(client);

		return api;
	}

	public PaymentWireCard moipCard(AccountWireCardRequest accountWireCard, CardRequest cardRequest) {

		String amountAki = null;
		String amountGreen = null;

		if (accountWireCard.getType().equals(AccountWireCardRequest.Type.MEMBER_FEE)) {
			// calcula total das taxas
			BigDecimal fixed1 = (new BigDecimal(accountWireCard.getValue()).multiply(PAYMENT_FEE_MEMBER_SHIP_GREEN))
					.divide(new BigDecimal(10000)).setScale(2, BigDecimal.ROUND_DOWN);

			// transforma em centavos
			amountGreen = fixed1.toString().replace(".", "");
			amountAki = "0";

		} else if (accountWireCard.getType().equals(AccountWireCardRequest.Type.CREDIT)) {
			amountGreen = "1";

		} else {
			// calcula total das taxas
			BigDecimal fixed1 = PAYMENT_FEE_AKI_1.add(PAYMENT_FEE_AKI_2).setScale(2, BigDecimal.ROUND_DOWN);
			;
			BigDecimal fixed2 = PAYMENT_FEE_GREEN_1.add(PAYMENT_FEE_GREEN_2).setScale(2, BigDecimal.ROUND_DOWN);
			;

			// transforma em centavos
			amountAki = fixed1.toString().replace(".", "");
			amountGreen = fixed2.toString().replace(".", "");

		}

		API api = this.buildSetup();
		Order createdOrder = null;
		try {

			OrderRequest a = new OrderRequest();
			a.ownId("ORD-" + System.currentTimeMillis());
			a.amount(new OrderAmountRequest().currency("BRL")
					.subtotals(new SubtotalsRequest().shipping(0).addition(0).discount(0)));
			a.customer(
					new CustomerRequest().ownId("CUS-" + System.currentTimeMillis()).fullname(accountWireCard.getName())
							.email(accountWireCard.getEmail()).birthdate(new ApiDateRequest().date(new Date()))
							.taxDocument(TaxDocumentRequest.cpf(accountWireCard.getCpf()))
							.phone(new PhoneRequest().setAreaCode("11").setNumber("55443322"))
							.shippingAddressRequest(new ShippingAddressRequest().street(accountWireCard.getStreet())
									.streetNumber(accountWireCard.getStreetNumber()).complement("")
									.city(accountWireCard.getCity()).state(accountWireCard.getState())
									.district(accountWireCard.getDistrict()).country("BRA")
									.zipCode(accountWireCard.getZipCode())));
			a.addItem(accountWireCard.getProduct(), 1, "Mais info...", new Integer(accountWireCard.getValue()));
			a.addReceiver(new ReceiverRequest().secondary("MPA-AF015D281353",
					new AmountRequest().fixed(new Integer(amountGreen)), true));
			createdOrder = api.order().create(a);
			System.out.println(createdOrder);
		} catch (UnauthorizedException e) {
			throw new CustomGenericUnauthorizedException("Error: not authorized: " + e.getMessage());
		} catch (UnexpectedException e) {
			throw new CustomGenericNotFoundException(e.getMessage());
		} catch (ValidationException e) {
			throw new CustomGenericNotFoundException("Error: " + e.getErrors());
		}

		try {
			br.com.moip.resource.Payment createdPayment = api.payment().create(new br.com.moip.request.PaymentRequest()
					.orderId(createdOrder.getId()) // Order's Moip ID
					.installmentCount(1)
					.fundingInstrument(new FundingInstrumentRequest().creditCard(new CreditCardRequest()
							.number(cardRequest.getCardNumber()).cvc(new Integer(cardRequest.getSecurityCode()))
							.expirationMonth(cardRequest.getExpirationMonth())
							.expirationYear(cardRequest.getExpirationYear())
							.holder(new HolderRequest().fullname("Jose Portador da Silva").birthdate("1988-10-10")
									.phone(new PhoneRequest().setAreaCode(accountWireCard.getAreaCode())
											.setNumber(accountWireCard.getNumber()))
									.taxDocument(TaxDocumentRequest.cpf("22222222222")))
							.store(true))));
			System.out.println(createdPayment);
			PaymentWireCard paymentWireCard = new PaymentWireCard();
			paymentWireCard.set_id(createdPayment.getId());
			paymentWireCard.setAmountPayment(createdPayment.getAmount().getTotal().toString());
			paymentWireCard.setAmountReceiver1(amountGreen);
			paymentWireCard.setAmountReceiver2(null);
			paymentWireCard.setStatus(createdPayment.getStatus().name());
			return paymentWireCard;

		} catch (UnauthorizedException e) {
			throw new CustomGenericUnauthorizedException("Error: not authorized: " + e.getMessage());
		} catch (UnexpectedException e) {
			throw new CustomGenericNotFoundException(e.getMessage());
		} catch (ValidationException e) {
			throw new CustomGenericNotFoundException("Error: " + e.getErrors());
		}
	}

	public void moipBoleto() {

		API api = this.buildSetup();

		Order createdOrder = api.order()
				.create(new OrderRequest().ownId("ORD-" + System.currentTimeMillis())
						.amount(new OrderAmountRequest().currency("BRL")
								.subtotals(new SubtotalsRequest().shipping(1000).addition(100).discount(500)))
						.addItem("Nome do produto 1", 1, "Mais info...", 100)
						.addItem("Nome do produto 2", 2, "Mais info...", 200)
						.addItem("Nome do produto 3", 3, "Mais info...", 300)
						.customer(new CustomerRequest().id("CUS-QAF1QEA23J7B")).addReceiver(new ReceiverRequest()
								.secondary("MPA-E3C8493A06AE", new AmountRequest().percentual(50), false)));

		try {

			br.com.moip.resource.Payment createdPayment = api.payment().create(new br.com.moip.request.PaymentRequest()
					.orderId(createdOrder.getId()) // Order's Moip ID
					.installmentCount(1)
					.fundingInstrument(new FundingInstrumentRequest().boleto(new BoletoRequest()
							.expirationDate(new ApiDateRequest()
									.date(new GregorianCalendar(2020, Calendar.NOVEMBER, 10).getTime()))
							.logoUri("http://logo.com").instructionLines(new InstructionLinesRequest()
									.first("Primeira linha").second("Segunda linha").third("Terceira linha")))));

			System.out.println(createdPayment);

		} catch (UnauthorizedException e) {
			System.out.println(e.getMessage());
		} catch (UnexpectedException e) {
			System.out.println(e.getMessage());
		} catch (ValidationException e) {
			System.out.println(e.getMessage());
		}

	}

	public br.com.moip.resource.Account createAccountWireCard(AccountWireCardRequest accountWireCard) {

		API api = this.buildSetup();
		try {

			br.com.moip.resource.Account account = api.account().create(new AccountRequest()
					.email(accountWireCard.getEmail()).type(AccountRequest.Type.MERCHANT).transparentAccount(true)
					.person(new PersonRequest().name(accountWireCard.getName()).lastName(accountWireCard.getLastName())
							.taxDocument(TaxDocumentRequest.cpf(accountWireCard.getCpf()))
							.birthDate(new ApiDateRequest()
									.date(new GregorianCalendar(1990, Calendar.JANUARY, 1).getTime()))
							.address(new ShippingAddressRequest().street(accountWireCard.getStreet())
									.streetNumber(accountWireCard.getStreetNumber())
									.district(accountWireCard.getDistrict()).city(accountWireCard.getCity())
									.state(accountWireCard.getState()).country(accountWireCard.getCountry())
									.zipCode(accountWireCard.getZipCode()))
							.phone(new PhoneRequest().countryCode(accountWireCard.getCountryCode())
									.setAreaCode(accountWireCard.getAreaCode()).setNumber(accountWireCard.getNumber()))
							.identityDocument(new IdentityDocumentRequest().number("434322344").issuer("SSP")
									.issueDate(new ApiDateRequest()
											.date(new GregorianCalendar(2000, Calendar.DECEMBER, 12).getTime()))
									.type(IdentityDocumentRequest.Type.RG))));

			System.out.println(account);
			return account;

		} catch (UnauthorizedException e) {
			throw new CustomGenericUnauthorizedException("Error: not authorized: " + e.getMessage());
		} catch (UnexpectedException e) {
			throw new CustomGenericNotFoundException(e.getMessage());
		} catch (ValidationException e) {
			throw new CustomGenericNotFoundException("Error: " + e.getErrors());
		}

	}

	public void createAccountGreenPay() {

		API api = this.buildSetup();
		try {

			br.com.moip.resource.Account account = api.account().create(new AccountRequest()
					.email("andrelfa3@gmail.com").type(AccountRequest.Type.MERCHANT).transparentAccount(true)
					.person(new PersonRequest().name("André Luis").lastName("Fialho")
							.taxDocument(TaxDocumentRequest.cpf("02236855001"))
							.birthDate(
									new ApiDateRequest().date(new GregorianCalendar(1993, Calendar.JULY, 19).getTime()))
							.address(new ShippingAddressRequest().street("Rua campos do Jordão bloco c ap 11")
									.streetNumber("200").district("Caxingui").city("SÃO PAULO").state("SP")
									.country("BRA").zipCode("05516-040"))
							.phone(new PhoneRequest().countryCode("55").setAreaCode("54").setNumber("981000227"))
							.identityDocument(new IdentityDocumentRequest().number("3106830197").issuer("SSP")
									.issueDate(new ApiDateRequest()
											.date(new GregorianCalendar(2011, Calendar.OCTOBER, 4).getTime()))
									.type(IdentityDocumentRequest.Type.RG))));

			System.out.println(account);

		} catch (UnauthorizedException e) {
			throw new CustomGenericUnauthorizedException("Error: not authorized: " + e.getMessage());
		} catch (UnexpectedException e) {
			throw new CustomGenericNotFoundException(e.getMessage());
		} catch (ValidationException e) {
			throw new CustomGenericNotFoundException("Error: " + e.getErrors());
		}

	}

}