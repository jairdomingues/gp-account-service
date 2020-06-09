package br.com.greenpay.core.system.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import br.com.greenpay.core.system.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	abstract Optional<Customer> findByDocument(String document);
	abstract Optional<Customer> findByUserId(String userId);

}
