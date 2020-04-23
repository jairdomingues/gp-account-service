package org.springframework.samples.petclinic.system;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.order.SalesOrder;
import org.springframework.samples.petclinic.order.SalesOrderResponse;
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
	
	public CustomerResponse findCustomerById(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
			.orElse(null);
		return convertToCustomerResponse(customer);
	}

	public CustomerResponse findCustomerByIdUser(String idUser) {
		Customer customer = customerRepository.findByIdUser(idUser)
			.orElse(null);
		if (customer == null) {
			return null;
		}
		return convertToCustomerResponse(customer);
	}

	private Customer convertToEntity(CustomerRequest customerRequest) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(customerRequest, Customer.class);
	}

	private CustomerResponse convertToCustomerResponse(Customer customer) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(customer, CustomerResponse.class);
	}

}
