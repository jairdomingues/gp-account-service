package org.springframework.samples.petclinic.partner;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PartnerAccountRepository extends CrudRepository<PartnerAccount, Long> {

	@Query("SELECT a FROM PartnerAccount a WHERE a.partner.id=:partnerId")
	PartnerAccount findPartnerAccountByPartner(@Param("partnerId") Long partnerId);

}
