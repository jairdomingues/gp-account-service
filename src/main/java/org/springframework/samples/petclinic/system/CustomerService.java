package org.springframework.samples.petclinic.system;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

	private static final Log LOGGER = LogFactory.getLog(CustomerService.class);

	@Autowired
	private CustomerRepository customerRepository;

	public void create(CustomerRequest customerRequest) {

		Optional<Customer> customerOptional = customerRepository.findByDocument(customerRequest.getDocument());
		if (customerOptional.isPresent()) {
			throw new CustomGenericNotFoundException("Error: Customer is already registered.");
		}
		Customer customer = this.convertToEntity(customerRequest);
		customer.getAdresses().stream().findFirst().get().setDefaults(true);
		WalletOfCustomer walletOfCustomer = new WalletOfCustomer();
		walletOfCustomer.setPassword(customerRequest.getPassword());
		walletOfCustomer.setWalletOfCustomer(customer);
		customer.setWalletOfCustomer(walletOfCustomer);
		customerRepository.save(customer);
		LOGGER.info(customerRequest.getFirstname());
	}

	private Customer convertToEntity(CustomerRequest customerRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(customerRequest, Customer.class);
	}

}
