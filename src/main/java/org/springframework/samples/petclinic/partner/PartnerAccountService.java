package org.springframework.samples.petclinic.partner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.partner.model.Partner;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory.Operation;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory.Status;
import org.springframework.samples.petclinic.partner.model.ReleaseHistory.TransactionType;
import org.springframework.samples.petclinic.partner.repository.PartnerAccountRepository;
import org.springframework.samples.petclinic.partner.repository.PartnerRepository;
import org.springframework.samples.petclinic.partner.response.PartnerAccountResponse;
import org.springframework.samples.petclinic.system.exception.CustomGenericNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartnerAccountService {

	private static final Log LOGGER = LogFactory.getLog(PartnerAccountService.class);

	@Autowired
	PartnerRepository partnerRepository;

	@Autowired
	PartnerAccountRepository partnerAccountRepository;

	public PartnerAccount createPartnerAccount(Long partnerId) {
		
		Partner partner = partnerRepository.findById(partnerId)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner is not found."));
		
		PartnerAccount partnerAccount = new PartnerAccount();
		partnerAccount.setActive(true);
		partnerAccount.setName(partner.getFantasia());
		partnerAccount.setPartner(partner);
		partnerAccountRepository.save(partnerAccount);
		return partnerAccount;
	}

	public void createReleaseHistory(Long idAccount, Operation operation, TransactionType transactionType, Status status,
			String history, BigDecimal amount, Long orderId) {

		PartnerAccount account = partnerAccountRepository.findById(idAccount)
				.orElseThrow(() -> new CustomGenericNotFoundException("Error: Partner Account is not found."));

//		if (account instanceof CurrentAccount) {
//			if (transactionType.equals(TransactionType.DEBIT) && amount.compareTo(account.getBalance()) == 1) {
//				throw new CustomGenericNotFoundException("Saldo insuficiente.");
//			}
//		}
		
		ReleaseHistory releaseHistory = new ReleaseHistory();
		releaseHistory.setOperation(operation);
		releaseHistory.setTransactionType(transactionType);
		releaseHistory.setStatus(status);
		releaseHistory.setHistory(history);
		releaseHistory.setAmount(amount);
		releaseHistory.setTransactionDate(new Date());
		//transactionHistory.setOrderId(orderId);

		List<ReleaseHistory> releases = account.getReleases() != null
				&& !account.getReleases().isEmpty() ? account.getReleases()
						: new ArrayList<ReleaseHistory>();
		releases.add(releaseHistory);
		account.setReleases(releases);
		partnerAccountRepository.save(account);

	}
	
	public PartnerAccountResponse findPartnerAccountByPartner(Long partnerId) {
		PartnerAccount partnerAccount = partnerAccountRepository.findPartnerAccountByPartner(partnerId);
		return convertToPartnerAccountResponse(partnerAccount);
	}
	
	private PartnerAccountResponse convertToPartnerAccountResponse(PartnerAccount partnerAccount) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(partnerAccount, PartnerAccountResponse.class);
	}

	
}
