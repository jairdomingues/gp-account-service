package org.springframework.samples.petclinic.partner.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.partner.model.Partner;

public interface PartnerRepository extends CrudRepository<Partner, Long> {

	abstract Optional<Partner> findByDocument(String document);
	abstract Optional<Partner> findByUserId(String userId);

}
