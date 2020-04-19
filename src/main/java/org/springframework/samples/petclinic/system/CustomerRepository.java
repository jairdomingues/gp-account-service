package org.springframework.samples.petclinic.system;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {

	abstract Optional<Customer> findByDocument(String document);

}
