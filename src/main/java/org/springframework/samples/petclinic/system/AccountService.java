package org.springframework.samples.petclinic.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
import org.springframework.samples.petclinic.order.SalesOrder;
import org.springframework.samples.petclinic.order.SalesOrderRepository;
import org.springframework.samples.petclinic.system.TransactionHistory.Operation;
import org.springframework.samples.petclinic.system.TransactionHistory.Status;
import org.springframework.samples.petclinic.system.TransactionHistory.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	private SalesOrderRepository salesOrderRepository;

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

		
		if (tokenAccountRequest.getType().equals(TokenAccount.Type.WALLET)) {
			//verificar a password para geração de token de WALLET 
			if (!account.getWalletHolder().getPassword().equals(tokenAccountRequest.getPassword())) {
				throw new CustomGenericNotFoundException("Error: Password is invalid.");
			}
		}
		TokenAccount tokenAccount = new TokenAccount();
		tokenAccount.setAccount(account);
		tokenAccount.setType(tokenAccountRequest.getType().equals(TokenAccount.Type.WALLET)?TokenAccount.Type.WALLET:TokenAccount.Type.SHARE);
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

}
