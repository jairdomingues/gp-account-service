package br.com.greenpay.core.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import br.com.greenpay.core.order.PaymentService;
import br.com.greenpay.core.order.request.PaymentRequest;
import br.com.greenpay.core.system.exception.CustomGenericNotFoundException;
import br.com.greenpay.core.system.model.Account;
import br.com.greenpay.core.system.model.CreditCard;
import br.com.greenpay.core.system.model.CurrentAccount;
import br.com.greenpay.core.system.model.Customer;
import br.com.greenpay.core.system.model.TokenAccount;
import br.com.greenpay.core.system.model.TransactionHistory;
import br.com.greenpay.core.system.model.Wallet;
import br.com.greenpay.core.system.model.TokenAccount.Type;
import br.com.greenpay.core.system.model.TransactionHistory.Operation;
import br.com.greenpay.core.system.model.TransactionHistory.Status;
import br.com.greenpay.core.system.model.TransactionHistory.TransactionType;
import br.com.greenpay.core.system.repository.AccountRepository;
import br.com.greenpay.core.system.repository.CustomerRepository;
import br.com.greenpay.core.system.repository.TokenAccountRepository;
import br.com.greenpay.core.system.request.CreditCardRequest;
import br.com.greenpay.core.system.request.CurrentAccountRequest;
import br.com.greenpay.core.system.request.DepositRequest;
import br.com.greenpay.core.system.request.TokenAccountRequest;
import br.com.greenpay.core.system.request.TransactionHistoryRequest;
import br.com.greenpay.core.system.request.TransferRequest;
import br.com.greenpay.core.system.request.WalletRequest;
import br.com.greenpay.core.system.response.AccountResponse;
import br.com.greenpay.core.system.response.TokenAccountResponse;

@Service
@Transactional
public class AccountService {

	private static final Log LOGGER = LogFactory.getLog(AccountService.class);

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TokenAccountRepository tokenAccountRepository;

	@Autowired
	private PaymentService paymentService;

	public List<AccountResponse> findAllAccounts() {
		List<Account> accounts = (List<Account>) accountRepository.findAll();
		return accounts.stream().sorted(Comparator.comparing(Account::getCreateDate).reversed())
				.map(this::convertToAccountResponse).collect(Collectors.toList());
	}

	public List<AccountResponse> findAllByCustomer(Long customerId) {
		List<Account> accounts = (List<Account>) accountRepository.findAllByCustomer(customerId);
		return accounts.stream().sorted(Comparator.comparing(Account::getCreateDate).reversed())
				.map(this::convertToAccountResponse).collect(Collectors.toList());
	}

	public List<AccountResponse> findAllByCpf(String cpf) {
		List<Account> accounts = (List<Account>) accountRepository.findAllByCpf(cpf);
		return accounts.stream().sorted(Comparator.comparing(Account::getCreateDate).reversed())
				.map(this::convertToAccountResponse).collect(Collectors.toList());
	}

	public void createCurrentAccount(CurrentAccountRequest currentAccountRequest) {
		CurrentAccount account = this.convertToCurrentAccount(currentAccountRequest);
		Customer customer = customerRepository.findById(currentAccountRequest.getCustomer().getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		CurrentAccount currentAccount = (CurrentAccount) accountRepository.findAccountByCurrentAccount(
				currentAccountRequest.getCurrentAccount(), currentAccountRequest.getAgency(),
				currentAccountRequest.getBankCode());
		if (currentAccount != null) {
			throw new CustomGenericNotFoundException("Error: Current Account is already registered.");
		}

		account.setCustomer(customer);
		account.setActive(true);
		account.setLastBalance(new BigDecimal(0.00));
		account.setWalletHolder(customer.getWalletOfCustomer());
		accountRepository.save(account);
		LOGGER.info(currentAccountRequest.getName());

		//para testes ...
		this.createTransactionHistory(account.getId(), Operation.DEPOSIT,
				TransactionType.CREDIT, Status.ACTIVE, "Recebimento deposito boleto  id "+1l,
				new BigDecimal(1000.00), 1l);
		
	}

	public void createCreditCard(CreditCardRequest creditCardRequest) {

		CreditCard account = this.convertToCreditCard(creditCardRequest);

		Customer customer = customerRepository.findById(creditCardRequest.getCustomer().getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		CreditCard creditCard = (CreditCard) accountRepository
				.findAccountByCardNumber(creditCardRequest.getCardNumber());
		if (creditCard != null) {
			throw new CustomGenericNotFoundException("Error: Credit Card is already registered.");
		}

		account.setCustomer(customer);
		account.setActive(true);
		account.setLastBalance(new BigDecimal(0.00));
		account.setWalletHolder(customer.getWalletOfCustomer());
		accountRepository.save(account);
		LOGGER.info(creditCardRequest.getName());

	}

	public void createWallet(WalletRequest walletRequest) {

		Wallet account = this.convertToWallet(walletRequest);

		Customer customer = customerRepository.findById(walletRequest.getCustomer().getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Customer is not found."));

		Wallet wallet = (Wallet) accountRepository.findAccountByHashCard(walletRequest.getHashCard());
		if (wallet != null) {
			throw new CustomGenericNotFoundException("Error: Wallet is already registered.");
		}

		account.setCustomer(customer);
		account.setActive(true);
		account.setLastBalance(new BigDecimal(0.00));
		account.setWalletHolder(customer.getWalletOfCustomer());
		accountRepository.save(account);
		LOGGER.info(walletRequest.getHashCard());

	}

	public TokenAccountResponse genereateToken(TokenAccountRequest tokenAccountRequest) {

		Account account = accountRepository.findById(tokenAccountRequest.getAccountId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));

		if (!tokenAccountRequest.getType().equals(TokenAccount.Type.SHARE)) {
			//verificar a password para geração de token de WALLET 
			if (!account.getWalletHolder().getPassword().equals(tokenAccountRequest.getPassword())) {
				throw new CustomGenericNotFoundException("Error: Password is invalid.");
			}
		}
		
		TokenAccount tokenAccount = new TokenAccount();
		tokenAccount.setAccount(account);
		tokenAccount.setType(tokenAccountRequest.getType());
		tokenAccount.setValid(true);
		tokenAccountRepository.save(tokenAccount);
		return convertToTokenAccountResponse(tokenAccount);
	}

	public void deleteById(Long idAccount) {
		Account account = accountRepository.findById(idAccount)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));

		if (!account.getTransactions().isEmpty()) {
			throw new CustomGenericNotFoundException("Error: Delete account is not possible.");
		}
		accountRepository.delete(account);
	}

	public void createTransactionHistory(TransactionHistoryRequest transactionHistoryRequest) {
		this.createTransactionHistory(transactionHistoryRequest.getIdAccount(), transactionHistoryRequest.getOperation(), transactionHistoryRequest.getTransactionType(), transactionHistoryRequest.getStatus(), transactionHistoryRequest.getHistory(), transactionHistoryRequest.getAmount(), transactionHistoryRequest.getOrderId());
	}

	public void createTransactionHistory(Long idAccount, Operation operation, TransactionType transactionType, TransactionHistory.Status status,
			String history, BigDecimal amount, Long orderId) {

		Account account = accountRepository.findById(idAccount)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));

		if (account instanceof CurrentAccount) {
			if (transactionType.equals(TransactionType.DEBIT) && amount.compareTo(account.getBalance()) == 1) {
				throw new CustomGenericNotFoundException("Saldo insuficiente.");
			}
		}
		TransactionHistory transactionHistory = new TransactionHistory();
		transactionHistory.setOperation(operation);
		transactionHistory.setTransactionType(transactionType);
		transactionHistory.setStatus(status);
		transactionHistory.setHistory(history);
		transactionHistory.setAmount(amount);
		transactionHistory.setTransactionDate(new Date());
		//transactionHistory.setOrderId(orderId);

		List<TransactionHistory> transactions = account.getTransactions() != null
				&& !account.getTransactions().isEmpty() ? account.getTransactions()
						: new ArrayList<TransactionHistory>();
		transactions.add(transactionHistory);
		account.setTransactions(transactions);
		accountRepository.save(account);

	}

	public TokenAccountResponse createTransfer(TransferRequest transferRequest) {

		TokenAccount tokenAccount = this.updateToken(transferRequest.getUuid());

		BigDecimal debitValue = transferRequest.getPayments().stream().map((e) -> e.getAmount()).reduce((sum, e) -> sum.add(e)).get();

		this.createTransactionHistory(tokenAccount.getAccount().getId(), Operation.TRANSFER,
				TransactionType.DEBIT, Status.ACTIVE, "Pagamento transferência id "+tokenAccount.getId(),
				debitValue, tokenAccount.getId());
		
		for (PaymentRequest paymentRequest : transferRequest.getPayments()) {
			
			Account account = accountRepository.findById(paymentRequest.getId())
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Account is not found."));
			
			this.createTransactionHistory(paymentRequest.getId(), Operation.TRANSFER,
					TransactionType.CREDIT, Status.ACTIVE, "Recebimento transferência id "+tokenAccount.getId(),
					debitValue, tokenAccount.getId());
		
		}
		
		TokenAccountResponse tokenAccountResponse = new TokenAccountResponse();
		tokenAccountResponse.setType(Type.TRANSFER);
		tokenAccountResponse.setUuid(tokenAccount.getUuid());
		return tokenAccountResponse;
	}

	public br.com.moip.resource.Payment createDeposit(DepositRequest depositRequest) {
		return paymentService.moipBoleto();
	}
	
	private CurrentAccount convertToCurrentAccount(CurrentAccountRequest accountRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(accountRequest, CurrentAccount.class);
	}

	private CreditCard convertToCreditCard(CreditCardRequest creditCardRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(creditCardRequest, CreditCard.class);
	}

	private Wallet convertToWallet(WalletRequest walletRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(walletRequest, Wallet.class);
	}

	private TokenAccountResponse convertToTokenAccountResponse(TokenAccount tokenAccount) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(tokenAccount, TokenAccountResponse.class);
	}

	private AccountResponse convertToAccountResponse(Account account) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(account, AccountResponse.class);
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public TokenAccount updateToken(String uuid) {
		TokenAccount tokenAccount = tokenAccountRepository.findByUuidValid(uuid)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Token is invalid."));
		tokenAccount.setValid(true);
		tokenAccountRepository.save(tokenAccount);
//		if (tokenAccount.getExpired()) {
//			throw new CustomGenericNotFoundException("Error: Token is expired.");
//		}
		return tokenAccount;
	}
	
	

}
