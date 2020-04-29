package org.springframework.samples.petclinic.partner;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.system.Customer;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

	abstract Optional<Partner> findByDocument(String document);
	abstract Optional<Partner> findByUserId(String userId);

}
