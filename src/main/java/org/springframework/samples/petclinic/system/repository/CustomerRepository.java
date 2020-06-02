package org.springframework.samples.petclinic.system.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.system.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	abstract Optional<Customer> findByDocument(String document);
	abstract Optional<Customer> findByUserId(String userId);

}
